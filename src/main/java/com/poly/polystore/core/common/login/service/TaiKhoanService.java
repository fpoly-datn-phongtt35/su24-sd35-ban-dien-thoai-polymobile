package com.poly.polystore.core.common.login.service;

import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaiKhoanService {
    private final TaiKhoanRepository taiKhoanRepository;

    public Optional<TaiKhoan> findByEmail(String email) {
        return taiKhoanRepository.findByEmail(email);
    }
    public TaiKhoan save(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }
}
