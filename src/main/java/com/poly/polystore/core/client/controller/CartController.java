package com.poly.polystore.core.client.controller;

import com.poly.polystore.entity.GioHang;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.GioHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import com.poly.polystore.utils.GetDataFromCookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class CartController {
    private final GetDataFromCookie getDataFromCookie;
    private final  KhachHangRepository khachHangRepository;
    private final GioHangRepository gioHangRepository;
    @GetMapping("/checkout")
    public String checkout(HttpServletRequest request, Model model) {
        TaiKhoan taiKhoan = getDataFromCookie.getTaiKhoan(request);
        List<GioHang> gioHangs = new ArrayList<>();
        if(taiKhoan != null) {
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(taiKhoan.getId());
            if(optionalKhachHang.isPresent()) {
                KhachHang khachHang = optionalKhachHang.get();
                gioHangs = gioHangRepository.findByIdKhachHang(khachHang.getId());
                model.addAttribute("gioHangs", gioHangs);
                model.addAttribute("khachHang", khachHang);
            }
        }
        else {
            gioHangs = getDataFromCookie.getDataFromCart(request);
            if(gioHangs != null){
                model.addAttribute("gioHangs",gioHangs);
            }
        }
        double total = 0;
        if (gioHangs != null) {
            for (GioHang gioHang : gioHangs) {
                total += gioHang.getSoLuong() * gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue();
            }
        }
        model.addAttribute("total", total);
        return "client/page/checkout";
    }
}
