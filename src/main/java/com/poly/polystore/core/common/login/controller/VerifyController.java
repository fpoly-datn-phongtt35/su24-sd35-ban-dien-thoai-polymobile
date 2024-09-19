package com.poly.polystore.core.common.login.controller;

import com.poly.polystore.core.common.login.model.request.VerifyUser;
import com.poly.polystore.core.common.login.service.JwtService;
import com.poly.polystore.core.common.login.service.TaiKhoanService;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.TaiKhoanRepository;
import com.poly.polystore.utils.SendMailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.expiration}")
    private Integer expiration;
    private final HttpServletRequest httpServletRequest;
    private final HttpSession httpSession;
    private final SendMailService sendMailService;
    private final TaiKhoanService taiKhoanService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final JwtService jwtService;

    @GetMapping("/verify-account")
    public String newAccount(Model model) {
        var ssTK = httpSession.getAttribute("newTaiKhoan");
        if (ssTK == null) {
            return "redirect:/sign-in";
        }
        var taiKhoan = (TaiKhoan) ssTK;


        if(httpSession.getAttribute("expirationTime")!=null){
            var time=(LocalDateTime) httpSession.getAttribute("expirationTime");
            if(LocalDateTime.now().isAfter(time)){//Hết 5p mới cấp mới
                var otp = String.format("%04d", new Random().nextInt(10000));
                var expirationTime = LocalDateTime.now().plusMinutes(5);
                sendOtp(taiKhoan.getEmail(), taiKhoan.getTen(), otp);
                httpSession.setAttribute("otp", otp);
                httpSession.setAttribute("expirationTime", expirationTime);
            }
        }else{// Chưa có cấp mới
            var otp = String.format("%04d", new Random().nextInt(10000));
            var expirationTime = LocalDateTime.now().plusMinutes(5);
            sendOtp(taiKhoan.getEmail(), taiKhoan.getTen(), otp);
            httpSession.setAttribute("otp", otp);
            httpSession.setAttribute("expirationTime", expirationTime);
        }


        model.addAttribute("newTaiKhoan", taiKhoan);
        if (taiKhoan.getOauths() == null || taiKhoan.getOauths().isEmpty()) {
            return "authentication/new-account-without-oauth";
        } else {
            return "authentication/new-account-with-oauth";
        }

    }

    @ResponseBody
    @PostMapping("/api/v2/verify-account-with-oauth")
    public ResponseEntity<?> verify(@Valid @RequestBody VerifyUser req, BindingResult result) {
        Object taiKhoan = httpSession.getAttribute("newTaiKhoan");
        Object otp = httpSession.getAttribute("otp");
        Object expirationTime = httpSession.getAttribute("expirationTime");
        if (taiKhoan == null || otp == null || expirationTime == null || LocalDateTime.now().isAfter((LocalDateTime) expirationTime)||!req.getOtp().equals(otp)) {
            result.addError(new ObjectError("otp", "Mã xác thực đã hết hạn"));
        }
        TaiKhoan tk = (TaiKhoan) taiKhoan;

        tk.setTen(req.getTen());
        tk.setSoDienThoai(req.getSoDienThoai());
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());

        }

        var saveTk = taiKhoanRepository.save(tk);
        destroyCreateAccount(saveTk);
        return ResponseEntity.ok(tk);
    }

    @ResponseBody
    @PostMapping("/api/v2/verify-account-without-oauth")
    public ResponseEntity<?> verify(@RequestParam(name = "otp") String reqOtp) {
        Object taiKhoan = httpSession.getAttribute("newTaiKhoan");
        Object otp = httpSession.getAttribute("otp");
        Object expirationTime = httpSession.getAttribute("expirationTime");
        if (taiKhoan == null || otp == null || expirationTime == null || LocalDateTime.now().isAfter((LocalDateTime) expirationTime)||!reqOtp.equals(otp)) {
            return ResponseEntity.badRequest().body("Mã xác thực không chính xác");
        }
        TaiKhoan tk = (TaiKhoan) taiKhoan;
        tk.setMatKhau(passwordEncoder.encode(tk.getMatKhau()));
        var saveTk = taiKhoanRepository.save(tk);
        destroyCreateAccount(saveTk);
        return ResponseEntity.ok(tk);
    }
    private void destroyCreateAccount(TaiKhoan taiKhoan){

        var jwt = jwtService.generateToken(taiKhoan);
        Cookie cookie = new Cookie("Authorization", jwt);
        cookie.setPath("/");
        cookie.setMaxAge(expiration); // 10 giờ
        httpServletResponse.addCookie(cookie);
        httpSession.removeAttribute("newTaiKhoan");
        httpSession.removeAttribute("otp");
        httpSession.removeAttribute("expirationTime");
    }
    @GetMapping("/reset-otp")
    public ResponseEntity<?> resetSend(){
        var taiKhoan = httpSession.getAttribute("newTaiKhoan");
        if (taiKhoan == null) {
            return ResponseEntity.badRequest().build();
        } else {
            var tk = (TaiKhoan) taiKhoan;

            var otp = new Random().nextInt(10000);
            sendOtp(tk.getEmail(), tk.getTen(), otp + "");
            httpSession.setAttribute("otp", otp);
            httpSession.setAttribute("expirationTime", LocalDateTime.now().plusMinutes(5));
            return ResponseEntity.ok().build();
        }

    }
    @GetMapping("/unauth-home")
    public String uauthhome() {
        if (httpSession.getAttribute("newTaiKhoan") != null) {
            return "redirect:/verify-account";
        }
        else{
            return "redirect:/home";
        }
    }
    @GetMapping("/home")
   public String home() {

        try {

            TaiKhoan taiKhoan = (TaiKhoan) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return switch (taiKhoan.getRole()) {
                case ROLE_ADMIN -> "redirect:/admin/statistic";
                case ROLE_EMPLOYEE -> "redirect:/admin/sale";
                default -> "redirect:/iphone";
            };
        } catch (Exception e) {
            return "redirect:/iphone";
        }

    }

    private void sendOtp(String to, String name, String otp){
        var subject = String.format("Mã bảo mật Tài khoản của bạn");
        var body = "Để xác nhận danh tính của bạn, chúng tôi cần xác minh địa chỉ email của bạn. Hãy dán mã này vào trình duyệt. Đây là mã dùng một lần.";
        var context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("to", to);
        context.setVariable("name", name);
        context.setVariable("content", body);
        try {
            sendMailService.send(to, subject, "mail/otp", context, null);
        } catch (Exception e) {
            System.out.println("Lỗi gửi maill thất bại");
        }
    }
}
