package com.poly.polystore.core.admin.don_hang.controller;

import com.azure.core.annotation.Post;
import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.core.admin.don_hang.dto.ImeiDTO;
import com.poly.polystore.core.admin.don_hang.dto.StatusOrderDetailDTO;
import com.poly.polystore.core.client.api.response.MagiamgiaResp;
import com.poly.polystore.core.client.dto.HoaDonChiTietDTO;
import com.poly.polystore.core.client.dto.HoaDonDTO;
import com.poly.polystore.core.client.dto.SanPhamChiTietDTO;
import com.poly.polystore.core.client.dto.SanPhamDTO;
import com.poly.polystore.core.client.service.IOrderService;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.*;
import com.poly.polystore.utils.FunctionUtil;
import com.poly.polystore.utils.SendMailUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
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
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private ThongTinBaoHanhRepository thongTinBaoHanhRepository;

    @Autowired
    private SendMailUtil sendMailUtil;

    @GetMapping("/don-hang")
    public String donHang(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                          @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<HoaDon> list = hoaDonRepository.findAll(PageRequest.of(page - 1, size));
        model.addAttribute("list", list);
        return "admin/cap-nhat-don-hang/danh-sach-don-hang";
    }

//    @GetMapping("/orders")
//    public String getOrderHistory(@RequestParam(value = "status", required = false) String status,
//                                  @RequestParam(value = "startDate", required = false) String startDate,
//                                  @RequestParam(value = "endDate", required = false) String endDate,
//                                  @RequestParam(value = "maDH", required = false) String maDH,
//                                  Authentication authentication,
//                                  Model model) {
//        List<HoaDonDTO> hoaDonDTOS = iOrderService.findOrderByUser(authentication, startDate, endDate, status, maDH);
//        model.addAttribute("orders", hoaDonDTOS);
//        return "/admin/don-hang/quanlydonhang";
//    }

    @GetMapping("/orders")
    public String getOrderHistory(@RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate,
                                  @RequestParam(value = "maDH", required = false) String maDH,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  Authentication authentication,
                                  Model model) {
        if(ObjectUtils.isEmpty(maDH))
            maDH = null;
        Page<HoaDon> hoaDonPage = iOrderService.findOrderByUserPage(authentication, startDate, endDate, status, maDH, page, size);

        List<HoaDon> hoaDons = hoaDonPage.getContent();
        List<HoaDonDTO> hoaDonDTOS = hoaDons.stream()
                .filter(hd -> hd.getHoaDonChiTiets().size()>0)
                .map(f -> modelMapper.map(f, HoaDonDTO.class))
                .collect(Collectors.toList());
        //retrive ten san pham
        hoaDonDTOS.forEach(hd -> {
            SanPhamDTO sanPham = hd.getHoaDonChiTiets().get(0)
                    .getSanPhamChiTiet().getSanpham();
            SanPhamChiTietDTO sanPhamChiTietDTO = hd.getHoaDonChiTiets().get(0)
                    .getSanPhamChiTiet();
            String tenSP = sanPham.getTenSanPham() + sanPham.getRam();
            String detailSP = sanPhamChiTietDTO.getMauSac().getTen();
            String leadingTop = String.join(" ", tenSP, detailSP);
            String imageUrl = sanPham.getAnh().getUrl();
            hd.setImageUrl(imageUrl);
            hd.setLeadingItem(leadingTop);
        });

        // count so luong
        for (HoaDonDTO hoaDonDTO : hoaDonDTOS) {
            List<HoaDonChiTietDTO> hoaDonChiTietDTOS = hoaDonDTO.getHoaDonChiTiets();
            Map<Integer, Integer> uniqueSPCT = new HashMap<>();
            Map<Integer, Integer> uniqueHDCT = new HashMap<>();
            for (HoaDonChiTietDTO hoaDonChiTietDTO : hoaDonChiTietDTOS) {
                Integer idSCPT = hoaDonChiTietDTO.getSanPhamChiTiet().getId();
                if (!uniqueSPCT.containsKey(idSCPT)) {
                    uniqueSPCT.put(idSCPT, 1);
                } else {
                    uniqueSPCT.put(idSCPT, uniqueSPCT.get(idSCPT) + 1);
                    uniqueHDCT.put(hoaDonChiTietDTO.getId(), idSCPT);
                }
            }
            hoaDonChiTietDTOS.removeIf(hdct -> uniqueHDCT.keySet().contains(hdct.getId()) && uniqueHDCT.values().contains(hdct.getSanPhamChiTiet().getId()));
            for (HoaDonChiTietDTO hoaDonChiTietDTO : hoaDonChiTietDTOS) {
                SanPhamChiTietDTO sanPhamChiTietDTO = hoaDonChiTietDTO.getSanPhamChiTiet();
                if (uniqueSPCT.keySet().contains(sanPhamChiTietDTO.getId())) {
                    hoaDonChiTietDTO.getSanPhamChiTiet().setSoLuongMua(uniqueSPCT.get(sanPhamChiTietDTO.getId()));
                } else hoaDonChiTietDTO.getSanPhamChiTiet().setSoLuongMua(1);
            }
            hoaDonDTO.setHoaDonChiTiets(hoaDonChiTietDTOS);
        }

        model.addAttribute("orders", hoaDonDTOS); // Lấy dữ liệu của trang hiện tại
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        model.addAttribute("totalItems", hoaDonPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("maDH", maDH);

        return "/admin/don-hang/quanlydonhang";
    }


    @GetMapping("/ordersv2")
    public String getOrderHistoryV2(@RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate,
                                  @RequestParam(value = "maDH", required = false) String maDH,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  Authentication authentication,
                                  Model model) {
        if(ObjectUtils.isEmpty(maDH))
            maDH = null;
        Page<HoaDon> hoaDonPage = iOrderService.findOrderByUserPage(authentication, startDate, endDate, status, maDH, page, size);

        List<HoaDon> hoaDons = hoaDonPage.getContent();
        List<HoaDonDTO> hoaDonDTOS = hoaDons.stream()
                .filter(hd -> hd.getHoaDonChiTiets().size()>0)
                .map(f -> modelMapper.map(f, HoaDonDTO.class))
                .collect(Collectors.toList());
        //retrive ten san pham
        hoaDonDTOS.forEach(hd -> {
            SanPhamDTO sanPham = hd.getHoaDonChiTiets().get(0)
                    .getSanPhamChiTiet().getSanpham();
            SanPhamChiTietDTO sanPhamChiTietDTO = hd.getHoaDonChiTiets().get(0)
                    .getSanPhamChiTiet();
            String tenSP = sanPham.getTenSanPham() + sanPham.getRam();
            String detailSP = sanPhamChiTietDTO.getMauSac().getTen();
            String leadingTop = String.join(" ", tenSP, detailSP);
            String imageUrl = sanPham.getAnh().getUrl();
            hd.setImageUrl(imageUrl);
            hd.setLeadingItem(leadingTop);
        });

        // count so luong
        for (HoaDonDTO hoaDonDTO : hoaDonDTOS) {
            List<HoaDonChiTietDTO> hoaDonChiTietDTOS = hoaDonDTO.getHoaDonChiTiets();
            Map<Integer, Integer> uniqueSPCT = new HashMap<>();
            Map<Integer, Integer> uniqueHDCT = new HashMap<>();
            for (HoaDonChiTietDTO hoaDonChiTietDTO : hoaDonChiTietDTOS) {
                Integer idSCPT = hoaDonChiTietDTO.getSanPhamChiTiet().getId();
                if (!uniqueSPCT.containsKey(idSCPT)) {
                    uniqueSPCT.put(idSCPT, 1);
                } else {
                    uniqueSPCT.put(idSCPT, uniqueSPCT.get(idSCPT) + 1);
                    uniqueHDCT.put(hoaDonChiTietDTO.getId(), idSCPT);
                }
            }
            hoaDonChiTietDTOS.removeIf(hdct -> uniqueHDCT.keySet().contains(hdct.getId()) && uniqueHDCT.values().contains(hdct.getSanPhamChiTiet().getId()));
            for (HoaDonChiTietDTO hoaDonChiTietDTO : hoaDonChiTietDTOS) {
                SanPhamChiTietDTO sanPhamChiTietDTO = hoaDonChiTietDTO.getSanPhamChiTiet();
                if (uniqueSPCT.keySet().contains(sanPhamChiTietDTO.getId())) {
                    hoaDonChiTietDTO.getSanPhamChiTiet().setSoLuongMua(uniqueSPCT.get(sanPhamChiTietDTO.getId()));
                } else hoaDonChiTietDTO.getSanPhamChiTiet().setSoLuongMua(1);
            }
            hoaDonDTO.setHoaDonChiTiets(hoaDonChiTietDTOS);
        }

        model.addAttribute("orders", hoaDonDTOS); // Lấy dữ liệu của trang hiện tại
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        model.addAttribute("totalItems", hoaDonPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("maDH", maDH);

        return "/admin/don-hang/quanlydonhang:: #orderList";
    }

    public Page<HoaDonDTO> convertPageToDto(Page<HoaDon> hoaDonPage) {
        return hoaDonPage.map(this::convertToDto);
    }
    public HoaDonDTO convertToDto(HoaDon hoaDon) {
        return modelMapper.map(hoaDon, HoaDonDTO.class);
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
        KhachHang khachHang;
        HoaDon hoaDon = hoaDonRepository.findById(Integer.parseInt(id)).get();
        Optional<KhachHang> khachHangOptional = khachHangRepository.findById(hoaDon.getKhachHang().getId());
        if(!khachHangOptional.isPresent()) return ResponseEntity.badRequest().body("Khách hàng không tồn tại");
        else khachHang = khachHangOptional.get();
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
                imeiRepository.saveAll(imeis);
                // giam so luong sanpham chi tiet
                hoaDon.getHoaDonChiTiets()
                        .stream()
                        .map(hdct -> hdct.getSanPhamChiTiet())
                        .forEach(spct -> {
                            spct.setSoLuong(spct.getSoLuong()-1);
                            sanPhamChiTietRepository.save(spct);
                        });
                break;
            case "Đang chuẩn bị hàng":
                trangthaidonhang = TRANGTHAIDONHANG.CHO_LAY_HANG;
                break;
            case "Chờ lấy hàng":
                trangthaidonhang = TRANGTHAIDONHANG.DANG_GIAO;
                try{
                    sendMailUtil.sendMailOrderDeliver(hoaDon, khachHang.getEmail());
                }catch (Exception e){
                    System.out.println("Faile send mail");
                }
                break;
            case "Đang Giao":
                trangthaidonhang = TRANGTHAIDONHANG.THANH_CONG;

                // thêm bảo hành
                HoaDon hoaDons = hoaDonRepository.findById(Integer.parseInt(id)).get();
                try{
                    List<HoaDonDTO> hoaDonDTOS = hoaDons.getHoaDonChiTiets()
                            .stream()
                            .map(f -> {
                                HoaDonDTO hoaDonDTO = modelMapper.map(f, HoaDonDTO.class);
                                hoaDonDTO.setIdKhachHang(String.valueOf(f.getHoaDon().getKhachHang().getId()));
                                return hoaDonDTO;
                            })
                            .collect(Collectors.toList());

                    // luu tung thong tin bao hanh
                    List<ThongTinBaoHanh> thongTinBaoHanhs = new ArrayList<>();
                    for(HoaDonDTO hoaDonDTO : hoaDonDTOS){
                        List<HoaDonChiTietDTO> hoaDonChiTietDTOS = hoaDonDTO.getHoaDonChiTiets();
                        for (HoaDonChiTietDTO hoaDonChiTietDTO : hoaDonChiTietDTOS){
                            ThongTinBaoHanh thongTinBaoHanh = new ThongTinBaoHanh();
                            if(hoaDonChiTietDTO.getImei()!=null){
                                thongTinBaoHanh.setImei(hoaDonChiTietDTO.getImei());
                            }
                            thongTinBaoHanh.setIdKhachHang(khachHang);
                            thongTinBaoHanh.setNgayBatDau(Instant.now());
                            thongTinBaoHanh.setThoiGianBaoHanh(FunctionUtil.getDate(Instant.now(), 2));
                            thongTinBaoHanh.setTrangThai("Chưa bảo hành");
                            thongTinBaoHanhs.add(thongTinBaoHanh);
                        }
                        thongTinBaoHanhRepository.saveAll(thongTinBaoHanhs);
                        sendMailUtil.sendMailOrderSuccess(hoaDon, khachHang.getEmail());
                    }
                }catch (Exception e){

                }
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
        if(!"Chờ xác nhận".equalsIgnoreCase(hoaDon.getTrangThai().getPriority()))
            return ResponseEntity.badRequest().body("Đơn hàng đã được xác nhận hoặc đã bị huỷ");
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

    @GetMapping("/order/cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam("orderId") Integer orderId) {
        HoaDon hoaDon = hoaDonRepository.findById(orderId).get();
        KhachHang khachHang;
        Optional<KhachHang> khachHangOptional = khachHangRepository.findById(hoaDon.getKhachHang().getId());
        if(!khachHangOptional.isPresent()) return ResponseEntity.badRequest().body("Khách hàng không tồn tại");
        else khachHang = khachHangOptional.get();
        // Lấy thông tin chi tiết đơn hàng bằng id
        String curentStatus = hoaDon.getTrangThai().getPriority();
        if("Giao Thành Công".equalsIgnoreCase(curentStatus)){
            return ResponseEntity.badRequest().body("Đơn hàng đã giao thành công!");
        }else
        {
            // huy don hang
            hoaDon.setTrangThai(TRANGTHAIDONHANG.DA_HUY);
            // update so luong san pham chi tiet
            hoaDon.getHoaDonChiTiets()
                    .stream()
                    .map(hdct -> hdct.getSanPhamChiTiet())
                    .forEach(spct -> {
                        spct.setSoLuong(spct.getSoLuong()+1);
                        sanPhamChiTietRepository.save(spct);
                    });

            // cap nhat imei trong kho
            List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTiets();
            List<Imei> imeis = hoaDonChiTiets.stream().map(hdct -> hdct.getImei()).collect(Collectors.toList());
            if(!ObjectUtils.isEmpty(imeis)){
                imeis.forEach(i -> i.setTrangThai(Imei.TrangThai.TRONG_KHO));
                imeiRepository.saveAll(imeis);
            }
            // tăng số lượng mã giảm giá
            List<Integer> sanPhamChiTietId = hoaDonChiTiets.stream()
                    .map(hdct -> hdct.getSanPhamChiTiet())
                    .map(spct -> spct.getDotGiamGia().getId())
                    .distinct()
                    .collect(Collectors.toList());
            List<PhieuGiamGia> phieuGiamGias = phieuGiamGiaRepository.findAllBySPCTID(sanPhamChiTietId);
            phieuGiamGias.forEach(p ->{
                        p.setSoluong(p.getSoluong() + 1);
                        phieuGiamGiaRepository.save(p);
                    });
            try{
                sendMailUtil.sendMailOrderCancel(hoaDon, khachHang.getEmail());
            }catch (Exception e){
                System.out.println("Faile send mail");
            }
        }
        return ResponseEntity.ok("Huỷ đơn hàng thành công");
    }
}
