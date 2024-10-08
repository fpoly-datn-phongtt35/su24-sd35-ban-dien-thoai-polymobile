package com.poly.polystore.utils;

import com.poly.polystore.core.common.login.service.JwtService;
import com.poly.polystore.entity.GioHang;
import com.poly.polystore.entity.SanPhamChiTiet;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.SanPhamChiTietRepository;
import com.poly.polystore.repository.TaiKhoanRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieUlti {
    private final JwtService jwtService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    public TaiKhoan getTaiKhoan(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        String token = null;
        for(Cookie item : cookies){
            if("Authorization".equals(item.getName())){
                token = item.getValue();
            }
        }
        if(token == null){
            return null;
        }
        String email = jwtService.extractEmail(token);
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(email);
        return optionalTaiKhoan.orElse(null);
    }

    public List<GioHang> getDataFromCart(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cart = null;
        if(cookies == null){
            return null;
        }
        for(Cookie item : cookies){
            if("Cart".equals(item.getName())){
                cart = item.getValue();
            }
        }
        if(cart == null){
            return null;
        }
        List<GioHang> list = new ArrayList<>();
        String[] strings = cart.trim().split("/");
        for(int i = 0;i < strings.length;i+=2){
            GioHang gioHang = new GioHang();
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(Integer.parseInt(strings[i])).get();
            gioHang.setIdSanPhamChiTiet(sanPhamChiTiet);
            gioHang.setSoLuong(Integer.parseInt(strings[i+1]));
            list.add(gioHang);
        }
        return list;
    }

    public void removeCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for(Cookie item : cookies){
            if("Cart".equals(item.getName())){
                item.setValue(null);
                item.setPath("/");
                item.setMaxAge(0);
                response.addCookie(item);
                request.getSession().removeAttribute("Cart");
                break;
            }
        }
    }
}
