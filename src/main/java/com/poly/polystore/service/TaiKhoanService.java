package com.poly.polystore.service;

import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaiKhoanService {
    private final TaiKhoanRepository taiKhoanRepository;
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return taiKhoanRepository.findBySoDienThoaiOrEmail().orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
            }
        };
    }
    public TaiKhoan save (TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }
}
