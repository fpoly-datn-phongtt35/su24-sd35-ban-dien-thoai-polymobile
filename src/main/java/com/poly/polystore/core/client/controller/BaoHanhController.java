package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.client.models.request.SearchForm;
import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.core.client.service.BaoHanhService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public String get(Model model, @PageableDefault(size = 1) Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int number = pageable.getPageNumber();
        List<WarrantyDTO> danhSachBaoHanh = baoHanhService.thongTinBaoHanh(null, null, null);
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
//        model.addAttribute("totalPages", danhSachBaoHanh.getTotalPages());
        return "/client/page/baohanh";
    }
    @PostMapping("/bao-hanh/search")
    public String getMonthlyRevenue(@RequestBody SearchForm searchForm,
                                    Model model) {
        String imei = null, phone = null;
        if("imei".equals(searchForm.getSearchType())){
            imei = searchForm.getSearchValue();
        }
        if("phone".equals(searchForm.getSearchType())){
            phone = searchForm.getSearchValue();
        }
        List<WarrantyDTO> danhSachBaoHanh = baoHanhService.thongTinBaoHanh(imei, null, phone);
        danhSachBaoHanh.forEach(w ->{
            w.setDiffTime(w.getThoiGianBH().getYear() - w.getNgayBD().getYear()+ " Năm");
        });
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
        return "/client/page/baohanh :: tableBody";
    }
}
