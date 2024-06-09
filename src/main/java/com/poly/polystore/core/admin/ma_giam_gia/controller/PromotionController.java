package com.poly.polystore.core.admin.ma_giam_gia.controller;

import com.poly.polystore.core.admin.ma_giam_gia.dto.CreatePromotionRequest;
import com.poly.polystore.entity.MaGiamGia;
import com.poly.polystore.repository.MaGiamGiaRepository;
import com.poly.polystore.repository.impl.MagiamgiaRepositoryImpl;
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
@RequestMapping("/admin/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final MagiamgiaRepositoryImpl maGiamGiaRepositoryImpl;
    private final MaGiamGiaRepository maGiamGiaRepository;
    @GetMapping("/list")
    public String promotions(Model model,@RequestParam(name = "page",defaultValue = "1") int page,
                             @RequestParam(name = "size",defaultValue = "5") int size,
                             @RequestParam(name = "code",required = false) String code,
                             @RequestParam(name = "beginDate",required = false) String beginDate,
                             @RequestParam(name = "endDate",required = false) String endDate) {
        Page<MaGiamGia> promotionPage = maGiamGiaRepositoryImpl.getMagiamgia(page, size, code, beginDate, endDate);
        model.addAttribute("promotionPage", promotionPage);
        return "admin/promotion/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        return "admin/promotion/create";
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody CreatePromotionRequest createPromotionRequest) {
        if(maGiamGiaRepository.findByCode(createPromotionRequest.getCode()) != null){
            throw new RuntimeException("Code đã tồn tại");
        }
        MaGiamGia maGiamGia = new MaGiamGia();
        maGiamGia.setCreateAt(Instant.now());
        maGiamGia.setCode(createPromotionRequest.getCode());
        maGiamGia.setGiamToiDa(new BigDecimal(createPromotionRequest.getMaxValue()));
        maGiamGia.setGiaTriToiThieu(new BigDecimal(createPromotionRequest.getMin()));
        maGiamGia.setSoLuong((int) createPromotionRequest.getSoluong());
        maGiamGia.setDeleted(false);
        maGiamGia.setThoiGianBatDau(createPromotionRequest.getBeginDate().toInstant());
        maGiamGia.setThoiGianKetThuc(createPromotionRequest.getExpiredDate().toInstant());
        maGiamGia.setPhanTramGiam((double) createPromotionRequest.getDiscountValue());
        maGiamGiaRepository.save(maGiamGia);
        return ResponseEntity.ok(maGiamGia.getId());
    }

    @GetMapping("/{id}")
    public String updatePromotionPage(Model model, @PathVariable long id) {
        MaGiamGia promotion = maGiamGiaRepository.findById((int)id).get();
        model.addAttribute("promotion", promotion);
        return "admin/promotion/edit";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePromotion(@Valid @RequestBody CreatePromotionRequest createPromotionRequest, @PathVariable long id) {
        MaGiamGia maGiamGia = maGiamGiaRepository.findById((int)id).get();
        maGiamGia.setCode(createPromotionRequest.getCode());
        maGiamGia.setGiamToiDa(new BigDecimal(createPromotionRequest.getMaxValue()));
        maGiamGia.setGiaTriToiThieu(new BigDecimal(createPromotionRequest.getMin()));
        maGiamGia.setSoLuong((int) createPromotionRequest.getSoluong());
        maGiamGia.setDeleted(false);
        maGiamGia.setThoiGianBatDau(createPromotionRequest.getBeginDate().toInstant());
        maGiamGia.setThoiGianKetThuc(createPromotionRequest.getExpiredDate().toInstant());
        maGiamGia.setPhanTramGiam((double) createPromotionRequest.getDiscountValue());
        maGiamGia.setUpdateAt(Instant.now());
        maGiamGiaRepository.save(maGiamGia);
        return ResponseEntity.ok(maGiamGia.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePromotion(@PathVariable long id) {
        MaGiamGia maGiamGia = maGiamGiaRepository.findById((int)id).get();
        maGiamGia.setDeleted(true);
        maGiamGiaRepository.save(maGiamGia);
        return ResponseEntity.ok("Xóa khuyến mại thành công");
    }
}
