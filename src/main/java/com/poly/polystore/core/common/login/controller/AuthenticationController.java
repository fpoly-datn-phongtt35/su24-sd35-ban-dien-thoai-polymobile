package com.poly.polystore.core.common.login.controller;

import com.poly.polystore.Constant.ROLE;
import com.poly.polystore.core.common.login.model.request.SignInRequest;
import com.poly.polystore.core.common.login.model.request.SignUpRequest;
import com.poly.polystore.core.common.login.model.response.JwtAuthenticationResponse;
import com.poly.polystore.core.common.login.service.AuthenticationService;
import com.poly.polystore.entity.TaiKhoan;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final HttpSession httpSession;

    @GetMapping("/sign-in")
    public String signin(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("signin_req", new SignInRequest());
        if (error != null) {
            switch (error) {
                case "401":{
                    model.addAttribute("message", "Bạn cần đăng nhập để truy cập trang này");
                    break;
                }
                case "404":{
                    model.addAttribute("message", "Tài khoản chưa tồn tại bạn cần đăng ký");
                    break;
                }
            }
        }
        return "authentication/authentication";
    }



    @ResponseBody
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signin(
            @RequestBody SignInRequest signInRequest,
            @Value("${jwt.expiration}") Integer expiration,
            HttpServletResponse response) {


        JwtAuthenticationResponse token = null;
        try {
            token = authenticationService.signin(signInRequest.getEmail(), signInRequest.getPassword());
            Cookie cookie = new Cookie("Authorization", token.getToken());
//        cookie.setSecure(true); // Đảm bảo cookie chỉ được gửi qua HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(expiration); // 10 giờ
            response.addCookie(cookie);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request,BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        var taiKhoan=new TaiKhoan();
        taiKhoan.setTen(request.getTen());
        taiKhoan.setSoDienThoai(request.getSoDienThoai());
        taiKhoan.setMatKhau(request.getMatKhau());
        taiKhoan.setEmail(request.getEmail());
        taiKhoan.setRole(ROLE.ROLE_CUSTOMER);
        taiKhoan.setTrangthai(true);
        httpSession.setAttribute("newTaiKhoan",taiKhoan);
        return ResponseEntity.ok().build();
    }


}
