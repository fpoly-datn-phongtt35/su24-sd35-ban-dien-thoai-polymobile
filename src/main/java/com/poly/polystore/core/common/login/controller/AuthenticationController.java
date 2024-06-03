package com.poly.polystore.core.common.login.controller;

import com.poly.polystore.core.common.login.model.request.SignInRequest;
import com.poly.polystore.core.common.login.model.request.SignUpRequest;
import com.poly.polystore.core.common.login.model.response.JwtAuthenticationResponse;
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
