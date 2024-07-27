package com.poly.polystore.core.client.api;

import com.poly.polystore.core.client.api.response.MagiamgiaResp;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.GioHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import com.poly.polystore.repository.MaGiamGiaRepository;
import com.poly.polystore.repository.PhieuGiamGiaRepository;
import com.poly.polystore.utils.CookieUlti;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client/magiamgia")
@RequiredArgsConstructor
public class MagiamgiaApi {
    private final MaGiamGiaRepository maGiamGiaRepository;
    private final CookieUlti cookieUlti;
    private final KhachHangRepository khachHangRepository;
    private final GioHangRepository gioHangRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    @PostMapping("/check")
    public ResponseEntity<?> checkMagiamgia(@RequestBody String code, HttpServletRequest request) {
        MaGiamGia magiamgia = maGiamGiaRepository.findByCode(code);
        if(magiamgia == null){
            return ResponseEntity.badRequest().body("Mã giảm giá không tồn tại");
        }
        else {
            if(magiamgia.getThoiGianBatDau().isAfter(Instant.now()) || magiamgia.getThoiGianKetThuc().isBefore(Instant.now())){
                return ResponseEntity.badRequest().body("Mã giảm giá không có hiệu lực");
            }
            TaiKhoan taiKhoan = cookieUlti.getTaiKhoan(request);
            List<GioHang> gioHangs = new ArrayList<>();
            if(taiKhoan != null) {
                Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(taiKhoan.getId());
                if(optionalKhachHang.isPresent()) {
                    KhachHang khachHang = optionalKhachHang.get();
                    gioHangs = gioHangRepository.findByIdKhachHang(khachHang.getId());
                }
            }
            else {
                gioHangs = cookieUlti.getDataFromCart(request);
            }
            List<PhieuGiamGia> list = phieuGiamGiaRepository.findAll().stream().filter(n -> n.getThoiGianBatDau().isBefore(Instant.now()) &&
                    n.getThoiGianKetThuc().isAfter(Instant.now()) && !n.getDeleted()).toList();
            if(!list.isEmpty()){
                PhieuGiamGia phieuGiamGia = list.get(0);
                for(GioHang gioHang : gioHangs) {
                    if(phieuGiamGia.getDonvi().equals("%")){
                        gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue() * (100 - phieuGiamGia.getGiaTriGiam().doubleValue()) / 100));
                    }
                    else {
                        gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue() - phieuGiamGia.getGiaTriGiam().doubleValue()));
                    }
                }
            }
            double total = 0;
            if (gioHangs != null) {
                for (GioHang gioHang : gioHangs) {
                    total += gioHang.getSoLuong() * gioHang.getRealPrice().doubleValue();
                }
            }
            if(total < magiamgia.getGiaTriToiThieu().doubleValue()){
                return ResponseEntity.badRequest().body("Đơn hàng của bạn không đủ điều kiện áp dụng mã giảm giá");
            }
            if(magiamgia.getSoLuong() == 0){
                return ResponseEntity.badRequest().body("Mã giảm giá không tồn tại");
            }
            double giamgia = Math.min(total * magiamgia.getPhanTramGiam(),magiamgia.getGiamToiDa().doubleValue());
            total -= giamgia;
            return ResponseEntity.ok(new MagiamgiaResp(magiamgia,total));
        }
    }
}
