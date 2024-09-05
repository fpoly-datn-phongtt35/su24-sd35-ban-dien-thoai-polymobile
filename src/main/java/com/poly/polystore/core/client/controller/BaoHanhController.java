package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.client.models.request.SearchForm;
import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.core.client.service.BaoHanhService;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.utils.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class BaoHanhController {
    @Autowired
    private BaoHanhService baoHanhService;
    @GetMapping("/bao-hanh")
    public String get(Model model,
                      Authentication authentication,
                      @PageableDefault(size = 1) Pageable pageable) {
        TaiKhoan taiKhoan = (TaiKhoan) authentication.getPrincipal();
        Integer idKH = taiKhoan == null ? null : taiKhoan.getId();
        List<WarrantyDTO> danhSachBaoHanh = baoHanhService.thongTinBaoHanh(null, idKH, null);
        danhSachBaoHanh.forEach(w -> w.setDiffTime(FunctionUtil.compareDate(w.getThoiGianBH(),w.getNgayBD())));
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
//        model.addAttribute("totalPages", danhSachBaoHanh.getTotalPages());
        return "/client/page/baohanh";
    }
    @PostMapping("/bao-hanh/search")
    public String getMonthlyRevenue(@RequestBody SearchForm searchForm,
                                    Authentication authentication,
                                    Model model) {
        TaiKhoan taiKhoan = (TaiKhoan) authentication.getPrincipal();
        Integer idKH = taiKhoan == null ? null : taiKhoan.getId();
        String imei = null, phone = null;
        String s = searchForm.getSearchType() == "" ? "imei" : searchForm.getSearchType();
        if("imei".equals(s)){
            imei = searchForm.getSearchValue();
        }
        if("phone".equals(s)){
            phone = searchForm.getSearchValue();
        }
        List<WarrantyDTO> danhSachBaoHanh = baoHanhService.thongTinBaoHanh(imei, idKH, phone);
        danhSachBaoHanh.forEach(w -> w.setDiffTime(FunctionUtil.compareDate(w.getThoiGianBH(),w.getNgayBD())));
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
        return "/client/page/baohanh :: tableBody";
    }
}
