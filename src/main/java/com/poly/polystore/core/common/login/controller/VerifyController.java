package com.poly.polystore.core.common.login.controller;

import com.poly.polystore.core.common.login.model.request.VerifyUser;
import com.poly.polystore.core.common.login.service.TaiKhoanService;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.utils.SendMailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class VerifyController {
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;
    private final HttpSession httpSession;
    private final SendMailService sendMailService;
    private final TaiKhoanService taiKhoanService;

    @GetMapping("/verify-account")
    public String newAccount(Model model) {
        var taiKhoan=(TaiKhoan) httpSession.getAttribute("newTaiKhoan");
        var otp=String.format("%04d",new Random().nextInt(10000));
        httpSession.setAttribute("otp",otp);
        var expirationTime= LocalDateTime.now().plusMinutes(5);
        httpSession.setAttribute("expirationTime",expirationTime);
        if(taiKhoan==null){
            return "redirect:/sign-in";
        }
        model.addAttribute("newTaiKhoan", taiKhoan);
        if(taiKhoan.getOauths()==null|| taiKhoan.getOauths().isEmpty()){
            return "authentication/new-account-without-oauth";
        }else{
            return "authentication/new-account-with-oauth";
        }

    }
    @ResponseBody
    @PostMapping("/verify-account")
    public ResponseEntity<?> createNewAccount(@Valid @RequestBody VerifyUser req, BindingResult result) {
        Object taiKhoan=httpSession.getAttribute("newTaiKhoan");
        Object otp= httpSession.getAttribute("otp");
        Object expirationTime= httpSession.getAttribute("expirationTime");
        if(taiKhoan==null||otp==null||expirationTime==null||LocalDateTime.now().isAfter((LocalDateTime) expirationTime)){
            result.addError(new ObjectError("otp","Mã xác thực đã hết hạn"));
        }
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }else{
//            taiKhoan.setTen(req.getTen());
//            taiKhoan.setTen(req.getEmail());
//            taiKhoan.setTen(req.getSoDienThoai());
//            taiKhoan.setTen(req.get());
        }
        return null;
    }

    @GetMapping("/reset-otp")
    public ResponseEntity<?> resetSend() throws MessagingException, IOException {
        var taiKhoan=httpSession.getAttribute("newTaiKhoan");
        if(taiKhoan==null){
            return ResponseEntity.badRequest().build();
        }else{
            var tk=(TaiKhoan) taiKhoan;

            var otp=new Random().nextInt(10000);
            sendOtp(tk.getEmail(),tk.getTen(),otp+"");
            httpSession.setAttribute("otp", otp);
            httpSession.setAttribute("expirationTime",LocalDateTime.now().plusMinutes(5));
            return ResponseEntity.ok().build();
        }

    }

    @GetMapping("/home")
    public String home() {

        if(httpSession.getAttribute("newTaiKhoan")!=null){
            return "redirect:/verify-account";
        }

        try{

            TaiKhoan taiKhoan=(TaiKhoan) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return switch (taiKhoan.getRole()){
                case ROLE_ADMIN -> "redirect:/admin/dashboard";
                case ROLE_EMPLOYEE -> "redirect:/admin/sale";
                default -> "redirect:/iphone";
            };
        }catch (Exception e){
            return "redirect:/iphone";
        }

    }
    private void sendOtp(String to,String name,String otp) throws MessagingException, IOException {
        var subject=String.format("Mã bảo mật Tài khoản của bạn");
        var body="Để xác nhận danh tính của bạn, chúng tôi cần xác minh địa chỉ email của bạn. Hãy dán mã này vào trình duyệt. Đây là mã dùng một lần.";
        var context=new Context();
        context.setVariable("otp",otp);
        context.setVariable("to",to);
        context.setVariable("name",name);
        context.setVariable("content",body);
        sendMailService.send(to,subject,"mail/otp",context,null);
    }
}
