package com.poly.polystore.core.common.login.controller;

import com.poly.polystore.core.admin.ma_giam_gia.dto.req.SignInRequest;
import com.poly.polystore.core.admin.ma_giam_gia.dto.req.SignUpRequest;
import com.poly.polystore.core.admin.ma_giam_gia.dto.resp.JwtAuthenticationResponse;
import com.poly.polystore.core.common.login.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/sign-in")
    public String signin() {
        return "authentication/authentication";
    }
    @ResponseBody
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest signInRequest) {
        System.out.println("-------------------------"+signInRequest);
       return authenticationService.signin(signInRequest.getEmail(),signInRequest.getPassword());
    }
    @ResponseBody
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }
}
