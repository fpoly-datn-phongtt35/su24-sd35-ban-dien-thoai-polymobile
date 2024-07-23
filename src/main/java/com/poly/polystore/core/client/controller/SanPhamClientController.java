package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.admin.san_pham.controller.SanPhamController;
import com.poly.polystore.entity.SanPham;
import com.poly.polystore.repository.SanPhamChiTietRepository;
import com.poly.polystore.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SanPhamClientController {


    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;


    @GetMapping("/iphone")
    public String demoClient2(Model model) {

        return "client/page/iphone";
    }
    @GetMapping("/iphone/{id}")
    public String getIphone(
            @PathVariable(name = "id") SanPham sp,
            Model model) {
        model.addAttribute("sp", sp);
        return "client/page/iphone-product";
    }



}
