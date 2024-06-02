package com.poly.polystore.controller;

import com.poly.polystore.dto.req.SignInRequest;
import com.poly.polystore.dto.req.SignUpRequest;
import com.poly.polystore.dto.resp.JwtAuthenticationResponse;
import com.poly.polystore.service.AuthenticationService;
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
