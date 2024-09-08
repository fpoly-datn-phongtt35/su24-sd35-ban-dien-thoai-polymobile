package com.poly.polystore.core.client.service.impl;

import com.poly.polystore.core.client.dto.HoaDonChiTietDTO;
import com.poly.polystore.core.client.dto.HoaDonDTO;
import com.poly.polystore.core.client.dto.SanPhamChiTietDTO;
import com.poly.polystore.core.client.dto.SanPhamDTO;
import com.poly.polystore.core.client.service.IOrderService;
import com.poly.polystore.entity.HoaDon;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.HoaDonRepository;
import com.poly.polystore.repository.KhachHangRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<HoaDonDTO> findOrderByUser(Authentication authentication, String startDate, String endDate, String status, String maDH) {
        TaiKhoan taiKhoan = (TaiKhoan) authentication.getPrincipal();
        Integer idKhachHang = khachHangRepository.findByIdTaiKhoan(taiKhoan.getId()).get().getId();

        // convert date
        LocalDate fromDate = null, toDate = null;
        if (!ObjectUtils.isEmpty(startDate))
            fromDate = LocalDate.parse(startDate);
        if (!ObjectUtils.isEmpty(endDate))
            toDate = LocalDate.parse(endDate);
        List<HoaDon> hoaDons = hoaDonRepository.getOrderByKHID(idKhachHang, "ALL".equals(status) ? null : status, fromDate, toDate, maDH);
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
        return hoaDonDTOS;
    }
}
