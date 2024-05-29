package com.poly.polystore.controller;

import com.poly.polystore.entity.DiaChiGiaoHang;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.repository.DiaChiGiaoHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/phieu-giao-hang")
public class PhieuGiaoHangController {
    @Autowired
    private DiaChiGiaoHangRepository diaChiGiaoHangRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @GetMapping("/create")
    public String create() {
        return "phieu-giao-hang/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam String name,@RequestParam String phone,
                         @RequestParam String address,@RequestParam String isDefault,
                         @RequestParam String idKhachhang) {
        DiaChiGiaoHang diaChiGiaoHang = new DiaChiGiaoHang();
        KhachHang khachHang = khachHangRepository.findById(Integer.parseInt(idKhachhang)).get();
        diaChiGiaoHang.setDiaChi(address);
        diaChiGiaoHang.setTenNguoiNhan(name);
        diaChiGiaoHang.setSoDienThoai(phone);
        diaChiGiaoHang.setLaDiaChiMacDinh("1".equals(isDefault));
        diaChiGiaoHang.setIdKhachHang(khachHang);
        diaChiGiaoHangRepository.save(diaChiGiaoHang);
        return "phieu-giao-hang/create";
    }
}
