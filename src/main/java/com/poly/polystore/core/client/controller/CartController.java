package com.poly.polystore.core.client.controller;


import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.*;
import com.poly.polystore.utils.CookieUlti;
import com.poly.polystore.utils.SendMailUtil;
import com.poly.polystore.utils.VNPAYService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CookieUlti cookieUlti;
    private final KhachHangRepository khachHangRepository;
    private final GioHangRepository gioHangRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final DiaChiGiaoHangRepository diaChiGiaoHangRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final VNPAYService vnpayService;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final SendMailUtil sendMailUtil;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("/checkout")
    public String checkout(HttpServletRequest request, Model model) {
        TaiKhoan taiKhoan = cookieUlti.getTaiKhoan(request);
        model.addAttribute("checked", false);
        List<GioHang> gioHangs = new ArrayList<>();
        if (taiKhoan != null) {
            model.addAttribute("checked", true);
            gioHangs = gioHangRepository.findByIdTaiKhoan(taiKhoan.getId());
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(taiKhoan.getId());
            if (optionalKhachHang.isPresent()) {
                KhachHang khachHang = optionalKhachHang.get();
                model.addAttribute("khachHang", khachHang);
                model.addAttribute("isChecked", "1");
            }
        } else {
            gioHangs = cookieUlti.getDataFromCart(request);
        }
        if (ObjectUtils.isEmpty(gioHangs)) {
            return "redirect:/iphone";
        }
        List<PhieuGiamGia> list = phieuGiamGiaRepository.findAll().stream().filter(n -> n.getThoiGianBatDau().isBefore(Instant.now()) &&
                n.getThoiGianKetThuc().isAfter(Instant.now()) && !n.getDeleted()).toList();

        if (!CollectionUtils.isEmpty(list)) {
            LOOP:
            for (GioHang gioHang : gioHangs) {
                for (PhieuGiamGia p : list) {
                    if (gioHang.getIdSanPhamChiTiet().getDotGiamGia() != null
                            && gioHang.getIdSanPhamChiTiet().getDotGiamGia().getId().equals(p.getId())
                            && p.getSoluong()!=null && p.getSoluong()>0
                            && Boolean.TRUE.equals(p.getStatus())
                    ) {
                        if (p.getDonvi().equals("%")) {
                            gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue() * (100 - p.getGiaTriGiam().doubleValue()) / 100).setScale(0, RoundingMode.FLOOR));
                            continue LOOP;
                        } else {
                            gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue() - p.getGiaTriGiam().doubleValue()));
                            continue LOOP;
                        }
                    } else
                    {
                        gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue()));
                    }
                }
            }
        } else {
            for (GioHang gioHang : gioHangs) {
                gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue()));
            }
        }
        double total = 0;
        for (GioHang gioHang : gioHangs) {
            gioHang.setSanPhamCungLoai(sanPhamChiTietRepository.findBySanPhamAndRom(gioHang.getIdSanPhamChiTiet().getSanPham(), gioHang.getIdSanPhamChiTiet().getRom()));
            total += gioHang.getSoLuong() * gioHang.getRealPrice().doubleValue();
        }
        model.addAttribute("gioHangs", gioHangs);
        model.addAttribute("total", total);
        return "client/page/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "address", required = false) String address, @RequestParam(value = "note", required = false) String note,
                           @RequestParam("payment") String payment, @RequestParam("discount-code") String code, @RequestParam("discount") String discount,
                           @RequestParam(value = "province", required = false) String province, @RequestParam(value = "city", required = false) String city,
                           @RequestParam(value = "street", required = false) String street, HttpServletRequest request,
                           @RequestParam(value = "email", required = false) String email, @RequestParam("shipping") String shipping,
                           @RequestParam(value = "iddiachi", required = false) String iddiachi, @RequestParam(value = "defaultAddress", required = false) String defaultAddress,
                           HttpServletResponse response, Model model) throws MessagingException, IOException {
        TaiKhoan taiKhoan = cookieUlti.getTaiKhoan(request);
        List<GioHang> gioHangs = new ArrayList<>();
        KhachHang khachHang = new KhachHang();
        if (taiKhoan != null) {
            gioHangs = gioHangRepository.findByIdTaiKhoan(taiKhoan.getId());
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(taiKhoan.getId());
            if (!optionalKhachHang.isPresent()) {
                khachHang = new KhachHang();
                khachHang.setEmail(email);
                khachHang.setSoDienThoai(phone);
                khachHang.setTen(name);
                khachHang.setIdTaiKhoan(taiKhoan);
                khachHang.setDeleted(0);
                khachHangRepository.save(khachHang);
                DiaChiGiaoHang diaChiGiaoHang = new DiaChiGiaoHang();
                diaChiGiaoHang.setProvince(province);
                diaChiGiaoHang.setWard(city);
                diaChiGiaoHang.setStreet(street);
                diaChiGiaoHang.setIdKhachHang(khachHang);
                diaChiGiaoHang.setLaDiaChiMacDinh("1".equals(defaultAddress));
                diaChiGiaoHangRepository.save(diaChiGiaoHang);
            } else {
                khachHang = optionalKhachHang.get();
                // case khach hang chưa có địa chỉ nào
                if ("1".equals(defaultAddress)) {
                    khachHang.getIdDiaChi().forEach(dc -> dc.setLaDiaChiMacDinh(false));
                    DiaChiGiaoHang diaChiGiaoHang = new DiaChiGiaoHang();
                    diaChiGiaoHang.setProvince(province);
                    diaChiGiaoHang.setWard(city);
                    diaChiGiaoHang.setTenNguoiNhan(khachHang.getTen());
                    diaChiGiaoHang.setSoDienThoai(khachHang.getSoDienThoai());
                    diaChiGiaoHang.setStreet(street);
                    diaChiGiaoHang.setIdKhachHang(khachHang);
                    diaChiGiaoHang.setLaDiaChiMacDinh(true);
                    diaChiGiaoHangRepository.save(diaChiGiaoHang);
                } else if (!"1".equals(defaultAddress) && !ObjectUtils.isEmpty(province)) {
                    DiaChiGiaoHang diaChiGiaoHang = new DiaChiGiaoHang();
                    diaChiGiaoHang.setProvince(province);
                    diaChiGiaoHang.setWard(city);
                    diaChiGiaoHang.setStreet(street);
                    diaChiGiaoHang.setTenNguoiNhan(khachHang.getTen());
                    diaChiGiaoHang.setSoDienThoai(khachHang.getSoDienThoai());
                    diaChiGiaoHang.setIdKhachHang(khachHang);
                    diaChiGiaoHang.setLaDiaChiMacDinh(false);
                    diaChiGiaoHangRepository.save(diaChiGiaoHang);
                }
                // case có nhiều địa chỉ và chọn lấy một cái set nó là địa chỉ mặc định
                if (!ObjectUtils.isEmpty(iddiachi)) {
                    DiaChiGiaoHang diaChiGiaoHang = diaChiGiaoHangRepository.findById(Integer.parseInt(iddiachi)).get();
                    diaChiGiaoHang.setLaDiaChiMacDinh(true);
                    khachHang.getIdDiaChi()
                            .stream()
                            .filter(dc -> dc.getId() != Integer.parseInt(iddiachi))
                            .forEach(dc -> dc.setLaDiaChiMacDinh(false));
                }
            }
        } else {
            khachHang = new KhachHang();
            khachHang.setEmail(email);
            khachHang.setSoDienThoai(phone);
            khachHang.setTen(name);
            khachHang.setDeleted(0);
            khachHangRepository.save(khachHang);
            DiaChiGiaoHang diaChiGiaoHang = new DiaChiGiaoHang();
            diaChiGiaoHang.setProvince(province);
            diaChiGiaoHang.setWard(city);
            diaChiGiaoHang.setStreet(street);
            diaChiGiaoHang.setIdKhachHang(khachHang);
            diaChiGiaoHang.setLaDiaChiMacDinh(false);
            diaChiGiaoHangRepository.save(diaChiGiaoHang);
            gioHangs = cookieUlti.getDataFromCart(request);
        }
        for (GioHang item : gioHangs) {
            if (item.getSoLuong() > item.getIdSanPhamChiTiet().getSoLuong()) {
                model.addAttribute("error", "Số lượng sản phẩm mua không được lớn hơn số lượng trong kho");
                return checkout(request, model);
            }
        }
        List<PhieuGiamGia> list = phieuGiamGiaRepository.findAll().stream().filter(n -> n.getThoiGianBatDau().isBefore(Instant.now()) &&
                n.getThoiGianKetThuc().isAfter(Instant.now()) && !n.getDeleted()).toList();

        if (!CollectionUtils.isEmpty(list)) {
            LOOP:
            for (GioHang gioHang : gioHangs) {
                for (PhieuGiamGia p : list) {
                    if (gioHang.getIdSanPhamChiTiet().getDotGiamGia() != null
                            && gioHang.getIdSanPhamChiTiet().getDotGiamGia().getId().equals(p.getId())
                            && p.getSoluong()!=null && p.getSoluong()>0
                            && Boolean.TRUE.equals(p.getStatus())
                    ) {
                        p.setSoluong(p.getSoluong()-1);
                        phieuGiamGiaRepository.save(p);
                        if (p.getDonvi().equals("%")) {
                            gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue() * (100 - p.getGiaTriGiam().doubleValue()) / 100).setScale(0, RoundingMode.FLOOR));
                            continue LOOP;
                        } else {
                            gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue() - p.getGiaTriGiam().doubleValue()));
                            continue LOOP;
                        }
                    } else
                    {
                        gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue()));
                    }
                }
            }
        } else {
            for (GioHang gioHang : gioHangs) {
                gioHang.setRealPrice(BigDecimal.valueOf(gioHang.getIdSanPhamChiTiet().getGiaBan().doubleValue()));
            }
        }
        double total = 0;
        for (GioHang gioHang : gioHangs) {
            total += gioHang.getSoLuong() * gioHang.getRealPrice().doubleValue();
        }
        Double shippingValue = shipping == "" ? 0 : Double.parseDouble(shipping.replaceAll("\\D", ""));
        Double giamVoucher = discount == "" ? 0 : Double.parseDouble(discount);

        if (ObjectUtils.isEmpty(address) && !ObjectUtils.isEmpty(iddiachi)) {
            address = diaChiGiaoHangRepository.findById(Integer.parseInt(iddiachi)).get().getDiaChi();
        }
        if (ObjectUtils.isEmpty(email)) {
            email = khachHang.getEmail();
        }
        total = total + shippingValue - giamVoucher;
        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(khachHang);
        hoaDon.setMaGiamGia(code);
        hoaDon.setTenNguoiNhan(name);
        hoaDon.setSoDienThoai(phone);
        hoaDon.setDiaChi(address);
        hoaDon.setTongSanPham(gioHangs.size());
        hoaDon.setPhiGiaoHang(new BigDecimal(shippingValue));
        hoaDon.setTrangThai(payment.equals("offline") ? TRANGTHAIDONHANG.CHO_XAC_NHAN : TRANGTHAIDONHANG.CHO_THANH_TOAN);
        hoaDon.setHinhThucGiaoHang("Giao tận nơi");
        hoaDon.setCreatedAt(Instant.now());
        hoaDon.setTongTienHoaDon(new BigDecimal(total));
        hoaDon.setHinhThucThanhToan(payment);
        hoaDon.setGiamVoucher(new BigDecimal(giamVoucher));
        hoaDon.setTrangThaiThanhToan("Chưa thanh toán");
        hoaDon.setNote(note);
        hoaDon.setEmail(email);
        hoaDonRepository.save(hoaDon);
        for (GioHang gioHang : gioHangs) {
//            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
//            hoaDonChiTiet.setIdSanPhamChiTiet(gioHang.getIdSanPhamChiTiet().getId());
//            hoaDonChiTiet.setIdHoaDon(hoaDon.getId());
//            hoaDonChiTiet.setGiaGoc(gioHang.getIdSanPhamChiTiet().getGiaBan());
//            hoaDonChiTiet.setGiaBan(gioHang.getRealPrice());
//            hoaDonChiTiet.setSoLuong(gioHang.getSoLuong());
//            hoaDonChiTietRepository.save(hoaDonChiTiet);
            //Loại bỏ số lượng vì đã có imei
            for (int i = 0; i < gioHang.getSoLuong(); i++) {
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                var spct = new SanPhamChiTiet();
                spct.setId(gioHang.getIdSanPhamChiTiet().getId());
                hoaDonChiTiet.setSanPhamChiTiet(spct);
                var hd = new HoaDon();
                hoaDonChiTiet.setHoaDon(hoaDon);
                hoaDonChiTiet.setGiaGoc(gioHang.getIdSanPhamChiTiet().getGiaBan());
                hoaDonChiTiet.setGiaBan(gioHang.getRealPrice());
                hoaDonChiTietRepository.save(hoaDonChiTiet);
            }

        }
        if (taiKhoan != null) {
            gioHangRepository.deleteAll(gioHangs);
        } else {
            cookieUlti.removeCookie(request, response);
        }
        try {
            sendMailUtil.sendMailOrder(hoaDon, email);
        } catch (Exception e) {
            log.error("send mail false: {}", e.getMessage());
        }
        if (payment.equals("offline")) {
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setIdHoaDon(hoaDon);
            lichSuHoaDon.setThoiGian(Instant.now());
            lichSuHoaDon.setTieuDe("Chờ xác nhận");
            lichSuHoaDonRepository.save(lichSuHoaDon);
            return "redirect:/iphone";
        } else {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnpayService.createOrder(request, (int) total, "Thanh toán cho đơn hàng " + hoaDon.getId(), baseUrl);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setIdHoaDon(hoaDon);
            lichSuHoaDon.setThoiGian(Instant.now());
            lichSuHoaDon.setTieuDe("Chờ Thanh Toán");
            lichSuHoaDonRepository.save(lichSuHoaDon);
            return "redirect:" + vnpayUrl;
        }
    }

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        int paymentStatus = vnpayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String id = orderInfo.substring("Thanh toán cho đơn hàng ".length()).trim();
        HoaDon hoaDon = hoaDonRepository.findById(Integer.parseInt(id)).get();
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setThoiGian(Instant.now());
        lichSuHoaDon.setIdHoaDon(hoaDon);
        if (paymentStatus == 1) {
            hoaDon.setTrangThai(TRANGTHAIDONHANG.CHO_XAC_NHAN);
            hoaDon.setTrangThaiThanhToan("Đã Thanh Toán");
            lichSuHoaDon.setTieuDe("Chờ xác nhận");
        } else {
            hoaDon.setTrangThai(TRANGTHAIDONHANG.DA_HUY);
            lichSuHoaDon.setTieuDe("Đơn hàng đã hủy");
        }
        hoaDonRepository.save(hoaDon);
        lichSuHoaDonRepository.save(lichSuHoaDon);
        return "redirect:/iphone";
    }
}
