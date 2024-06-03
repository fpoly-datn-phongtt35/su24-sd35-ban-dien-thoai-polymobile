package com.poly.polystore.core.common.login.service;


import com.poly.polystore.core.admin.ma_giam_gia.dto.req.SignUpRequest;
import com.poly.polystore.core.admin.ma_giam_gia.dto.resp.JwtAuthenticationResponse;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TaiKhoanService taiKhoanService;

    public JwtAuthenticationResponse signup(SignUpRequest req)
    {
        var taiKhoan = TaiKhoan
                .builder()
                .soDienThoai(req.getSoDienThoai())
                .ten(req.getTen())
                .email(req.getEmail())
                .matKhau(passwordEncoder.encode(req.getMatKhau()))
                .build();

        taiKhoan = taiKhoanService.save(taiKhoan);
        var jwt = jwtService.generateToken(taiKhoan);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    public JwtAuthenticationResponse signin(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password));
        var taiKhoan = taiKhoanService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid phone number or password."));

        var jwt = jwtService.generateToken(taiKhoan);
        return JwtAuthenticationResponse.builder().token(jwt).build();

    }

}