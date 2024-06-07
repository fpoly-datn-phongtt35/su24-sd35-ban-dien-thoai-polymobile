package com.poly.polystore.core.admin.phieu_giam_gia.controller;

import com.poly.polystore.core.admin.phieu_giam_gia.dto.CreatePhieuGiamGiaRequest;
import com.poly.polystore.entity.PhieuGiamGia;
import com.poly.polystore.repository.PhieuGiamGiaRepository;
import com.poly.polystore.repository.impl.PhieuGiamGiaRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class PhieuGiamGiaController {
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final PhieuGiamGiaRepositoryImpl giamGiaRepository;
    @GetMapping("/admin/phieugiamgia/list")
    public String promotions(Model model, @RequestParam(name = "page",defaultValue = "1") int page,
                             @RequestParam(name = "size",defaultValue = "5") int size,
                             @RequestParam(name = "code",required = false) String code,
                             @RequestParam(name = "beginDate",required = false) String beginDate,
                             @RequestParam(name = "endDate",required = false) String endDate) {
        Page<PhieuGiamGia> promotionPage = giamGiaRepository.getMagiamgia(page, size, code, beginDate, endDate);
        model.addAttribute("promotionPage", promotionPage);
        return "admin/phieu-giam-gia/list";
    }

    @GetMapping("/admin/phieugiamgia/create")
    public String create(Model model) {
        return "admin/phieu-giam-gia/create";
    }

    @PostMapping("/admin/phieugiamgia/create")
    public ResponseEntity<Object> create(@Valid @RequestBody CreatePhieuGiamGiaRequest createPhieuGiamGiaRequest) {
        if(phieuGiamGiaRepository.findByCode(createPhieuGiamGiaRequest.getCode()) == null){
            throw new RuntimeException("Code đã tồn tại");
        }
        PhieuGiamGia phieuGiamGia = new PhieuGiamGia();
        phieuGiamGia.setCreateAt(Instant.now());
        phieuGiamGia.setCode(createPhieuGiamGiaRequest.getCode());
        phieuGiamGia.setGiaTriGiam(new BigDecimal(createPhieuGiamGiaRequest.getDiscountValue()));
        phieuGiamGia.setDonvi(createPhieuGiamGiaRequest.getType());
        phieuGiamGia.setDeleted(false);
        phieuGiamGia.setThoiGianBatDau(createPhieuGiamGiaRequest.getBeginDate().toInstant());
        phieuGiamGia.setThoiGianKetThuc(createPhieuGiamGiaRequest.getExpiredDate().toInstant());
        phieuGiamGiaRepository.save(phieuGiamGia);
        return ResponseEntity.ok(phieuGiamGia.getId());
    }

    @GetMapping("/admin/phieugiamgia/update/{id}")
    public String updatePromotionPage(Model model, @PathVariable long id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById((int)id).get();
        model.addAttribute("phieuGiamGia", phieuGiamGia);
        return "admin/phieu-giam-gia/edit";
    }

    @PutMapping("/api/admin/phieugiamgia/{id}")
    public ResponseEntity<Object> updatePromotion(@Valid @RequestBody CreatePhieuGiamGiaRequest createPhieuGiamGiaRequest, @PathVariable long id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById((int)id).get();
        phieuGiamGia.setUpdateAt(Instant.now());
        phieuGiamGia.setCode(createPhieuGiamGiaRequest.getCode());
        phieuGiamGia.setGiaTriGiam(new BigDecimal(createPhieuGiamGiaRequest.getDiscountValue()));
        phieuGiamGia.setDonvi(createPhieuGiamGiaRequest.getType());
        phieuGiamGia.setDeleted(false);
        phieuGiamGia.setThoiGianBatDau(createPhieuGiamGiaRequest.getBeginDate().toInstant());
        phieuGiamGia.setThoiGianKetThuc(createPhieuGiamGiaRequest.getExpiredDate().toInstant());
        phieuGiamGiaRepository.save(phieuGiamGia);
        return ResponseEntity.ok(phieuGiamGia.getId());
    }

    @DeleteMapping("/api/admin/phieugiamgia/{id}")
    public ResponseEntity<Object> deletePromotion(@PathVariable long id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById((int)id).get();
        phieuGiamGia.setDeleted(true);
        phieuGiamGiaRepository.save(phieuGiamGia);
        return ResponseEntity.ok("Xóa khuyến mại thành công");
    }
}
