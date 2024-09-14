package com.poly.polystore.core.admin.bao_hanh;

import com.poly.polystore.core.client.models.request.SearchForm;
import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.core.client.service.BaoHanhService;
import com.poly.polystore.entity.ThongTinBaoHanh;
import com.poly.polystore.repository.ThongTinBaoHanhRepository;
import com.poly.polystore.utils.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BaoHanhControllerV2 {
    @Autowired
    private BaoHanhService baoHanhService;
    @Autowired
    private ThongTinBaoHanhRepository thongTinBaoHanhRepository;
    @GetMapping("/baohanhv2")
    public String get(Model model) {
        List<WarrantyDTO> danhSachBaoHanh = baoHanhService.thongTinBaoHanh(null, null, null);
        danhSachBaoHanh.forEach(w -> {
            w.setDiffTime(FunctionUtil.compareDate(w.getThoiGianBH(),w.getNgayBD()));
            w.setTrangThai(FunctionUtil.getStatus(w));
        });
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
        return "/admin/bao-hanh/bao-hanh";
    }
    @PostMapping(value = "/baohanhv2/search", consumes = "application/json", produces = "application/json")
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
        danhSachBaoHanh.forEach(w -> {
            w.setDiffTime(FunctionUtil.compareDate(w.getThoiGianBH(),w.getNgayBD()));
            w.setTrangThai(FunctionUtil.getStatus(w));
        });
        model.addAttribute("danhSachBaoHanh", danhSachBaoHanh);
        return "/admin/bao-hanh/bao-hanh :: tableBody";
    }
    @PostMapping("/warranty/update")
    @ResponseBody
    public ResponseEntity<?> updateWarranty(@RequestBody WarrantyRequest warrantyRequest) {
        // Tìm sản phẩm chi tiết theo ID
        Optional<ThongTinBaoHanh> orderDetailOptional = thongTinBaoHanhRepository.findById(warrantyRequest.getItemId());

        if (orderDetailOptional.isPresent()) {
            ThongTinBaoHanh orderDetail = orderDetailOptional.get();
            // Cập nhật trạng thái bảo hành
            orderDetail.setTrangThai("Đã bảo hành");
            orderDetail.setReason(warrantyRequest.getReason());
            thongTinBaoHanhRepository.save(orderDetail);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại");
        }
    }
}
