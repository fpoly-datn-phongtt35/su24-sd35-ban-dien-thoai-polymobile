package com.poly.polystore.core.common.login.controller;

import com.poly.polystore.core.common.login.model.request.SignInRequest;
import com.poly.polystore.core.common.login.model.request.SignUpRequest;
import com.poly.polystore.core.common.login.model.response.JwtAuthenticationResponse;
import com.poly.polystore.core.common.login.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/sign-in")
    public String signin(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("signin_req", new SignInRequest());
        if (error != null) {
            switch (error) {
                case "401":
                    model.addAttribute("message", "Trước tiên bạn cần đăng nhập");
            }
        }
        return "authentication/authentication";
    }

    @ResponseBody
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signin(
            @RequestBody SignInRequest signInRequest,
            @Value("${jwt.expiration}") Integer expiration,
            HttpServletResponse response) {


        var token = authenticationService.signin(signInRequest.getEmail(), signInRequest.getPassword());
        Cookie cookie = new Cookie("Authorization", token.getToken());
//        cookie.setSecure(true); // Đảm bảo cookie chỉ được gửi qua HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(expiration); // 10 giờ
        response.addCookie(cookie);
        return token;
    }

    @ResponseBody
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }
}
