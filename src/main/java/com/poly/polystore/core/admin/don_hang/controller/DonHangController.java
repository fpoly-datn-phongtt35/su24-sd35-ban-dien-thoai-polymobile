package com.poly.polystore.core.admin.don_hang.controller;

import com.poly.polystore.entity.HoaDon;
import com.poly.polystore.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/don-hang")
public class DonHangController {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping("")
    public String donHang(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                          @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<HoaDon> list = hoaDonRepository.findAll(PageRequest.of(page - 1,size));
        model.addAttribute("list", list);
        return "admin/cap-nhat-don-hang/danh-sach-don-hang";
    }

}
