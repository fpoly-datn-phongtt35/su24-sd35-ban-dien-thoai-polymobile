package com.poly.polystore.core.admin.bao_hanh;

import com.poly.polystore.core.client.models.request.SearchForm;
import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.core.client.service.BaoHanhService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BaoHanhControllerV2 {
    @Autowired
    private BaoHanhService baoHanhService;
    @GetMapping("/baohanhv2")
    public String get(Model model, @PageableDefault(size = 1) Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int number = pageable.getPageNumber();
        List<WarrantyDTO> danhSachBaoHanh = baoHanhService.thongTinBaoHanh(null, null, null);
        danhSachBaoHanh.forEach(w ->{
            w.setDiffTime(w.getThoiGianBH().getYear() - w.getNgayBD().getYear()+ " Năm");
        });
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
//        model.addAttribute("totalPages", danhSachBaoHanh.getTotalPages());
        return "/admin/bao-hanh/bao-hanh";
    }
    @PostMapping(value = "/baohanhv2/search", consumes = "application/json", produces = "application/json")
    public String getMonthlyRevenue(@RequestBody SearchForm searchForm,
                                    Model model) {
//    public String getMonthlyRevenue(@RequestParam(name = "searchType", required = false) String searchType,
//                                    @RequestParam(name = "searchValue", required = false)  String searchValue,
//                                    Model model) {
        String imei = null, phone = null;
        if("imei".equals(searchForm.getSearchType())){
            imei = searchForm.getSearchValue();
        }
        if("phone".equals(searchForm.getSearchType())){
            phone = searchForm.getSearchValue();
        }
//        if("imei".equals(searchType)){
//            imei = searchValue;
//        }
//        if("phone".equals(searchType)){
//            phone = searchValue;
//        }
        List<WarrantyDTO> danhSachBaoHanh = baoHanhService.thongTinBaoHanh(imei, null, phone);
        danhSachBaoHanh.forEach(w ->{
            w.setDiffTime(w.getThoiGianBH().getYear() - w.getNgayBD().getYear()+ " Năm");
        });
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
        return "/admin/bao-hanh/bao-hanh :: tableBody";
    }
}
