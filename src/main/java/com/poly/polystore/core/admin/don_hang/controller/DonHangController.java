package com.poly.polystore.core.admin.don_hang.controller;

import com.azure.core.annotation.Post;
import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.core.admin.don_hang.dto.ImeiDTO;
import com.poly.polystore.core.admin.don_hang.dto.StatusOrderDetailDTO;
import com.poly.polystore.core.client.api.response.MagiamgiaResp;
import com.poly.polystore.core.client.dto.HoaDonChiTietDTO;
import com.poly.polystore.core.client.dto.HoaDonDTO;
import com.poly.polystore.core.client.service.IOrderService;
import com.poly.polystore.entity.HoaDon;
import com.poly.polystore.entity.HoaDonChiTiet;
import com.poly.polystore.entity.Imei;
import com.poly.polystore.repository.HoaDonChiTietRepository;
import com.poly.polystore.repository.HoaDonRepository;
import com.poly.polystore.repository.ImeiRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class DonHangController {
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImeiRepository imeiRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @GetMapping("/don-hang")
    public String donHang(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                          @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<HoaDon> list = hoaDonRepository.findAll(PageRequest.of(page - 1, size));
        model.addAttribute("list", list);
        return "admin/cap-nhat-don-hang/danh-sach-don-hang";
    }

    @GetMapping("/orders")
    public String getOrderHistory(@RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate,
                                  @RequestParam(value = "maDH", required = false) String maDH,
                                  Authentication authentication,
                                  Model model) {
        List<HoaDonDTO> hoaDonDTOS = iOrderService.findOrderByUser(authentication, startDate, endDate, status, maDH);
        model.addAttribute("orders", hoaDonDTOS);
        return "/admin/don-hang/quanlydonhang";
    }

    @GetMapping("/order/detail/{id}")
    public String showOrderDetail(@PathVariable("id") Integer id, Model model) {
        // Lấy thông tin chi tiết đơn hàng bằng id
        HoaDonDTO hoaDonDTO = iOrderService.findOrderById(id);
        model.addAttribute("order", hoaDonDTO);
        return "/admin/don-hang/chitiet";
    }

    @GetMapping("/order/change-status")
    public ResponseEntity<?> showOrderDetail(@RequestParam String status, @RequestParam("orderId") String id) {
        // Lấy thông tin chi tiết đơn hàng bằng id
        HoaDon hoaDon = hoaDonRepository.findById(Integer.parseInt(id)).get();
        TRANGTHAIDONHANG trangthaidonhang = null;
        switch (status) {
            case "Chờ xác nhận":
                trangthaidonhang = TRANGTHAIDONHANG.XAC_NHAN;
                break;
            case "Xác nhận":
                trangthaidonhang = TRANGTHAIDONHANG.DANG_CHUAN_BI_HANG;
                List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTiets();
                List<Imei> imeis = hoaDonChiTiets.stream().map(hdct -> hdct.getImei()).collect(Collectors.toList());
                imeis.forEach(i -> i.setTrangThai(Imei.TrangThai.DA_BAN));
                break;
            case "Đang chuẩn bị hàng":
                trangthaidonhang = TRANGTHAIDONHANG.CHO_LAY_HANG;
                break;
            case "Chờ lấy hàng":
                trangthaidonhang = TRANGTHAIDONHANG.DANG_GIAO;
                break;
            case "Đang Giao":
                trangthaidonhang = TRANGTHAIDONHANG.THANH_CONG;
                break;
            default:
                break;
        }
        hoaDon.setTrangThai(trangthaidonhang);
        hoaDonRepository.save(hoaDon);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PostMapping("order/check-imei")
    public ResponseEntity<?> checkImei(@RequestBody ImeiDTO imeiDTO) {
        List<HoaDonChiTiet> hoaDonChiTiets = new ArrayList<>();
        for(String imei : imeiDTO.getImei())
        {
            Imei existByImeiAndSPCTID = imeiRepository.findByImeiAndSanPhamChiTiet(imei, imeiDTO.getProductId());
            if (!ObjectUtils.isEmpty(existByImeiAndSPCTID)) {
                HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonCTAndSPCTId(imeiDTO.getOrderDetailId(), imeiDTO.getProductId());
                hoaDonChiTiet.setImei(existByImeiAndSPCTID);
                hoaDonChiTiets.add(hoaDonChiTiet);
            }else
                return ResponseEntity.badRequest().body(String.format("Mã imei %s không tồn tại", imei));
        }
        hoaDonChiTietRepository.saveAll(hoaDonChiTiets);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/order/check-status")
    public ResponseEntity<?> checkValidOrder(@RequestParam("orderId") String id) {
        // Lấy thông tin chi tiết đơn hàng bằng id
        HoaDon hoaDon = hoaDonRepository.findById(Integer.parseInt(id)).get();
        HoaDonDTO hoaDonDTO = modelMapper.map(hoaDon, HoaDonDTO.class);
        List<HoaDonChiTietDTO> hoaDonChiTietDTOS = hoaDonDTO.getHoaDonChiTiets()
                .stream()
                .filter(hdct -> ObjectUtils.isEmpty(hdct.getImei()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(hoaDonChiTietDTOS)) {
            List<Integer> orderIds = hoaDonDTO.getHoaDonChiTiets().stream().map(hdct -> hdct.getId()).collect(Collectors.toList());
            return ResponseEntity.ok(orderIds);
        }
        return ResponseEntity.badRequest().body("Vui lòng nhập đủ Imei cho sản phẩm");
    }
}
