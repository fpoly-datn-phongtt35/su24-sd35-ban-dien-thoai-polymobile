package com.poly.polystore.core.client.service.impl;

import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.core.client.dto.HoaDonChiTietDTO;
import com.poly.polystore.core.client.dto.HoaDonDTO;
import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.core.client.service.BaoHanhService;
import com.poly.polystore.entity.HoaDon;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.entity.ThongTinBaoHanh;
import com.poly.polystore.repository.HoaDonRepository;
import com.poly.polystore.repository.KhachHangRepository;
import com.poly.polystore.repository.ThongTinBaoHanhRepository;
import com.poly.polystore.utils.FunctionUtil;
import org.apache.poi.ss.formula.functions.T;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaoHanhServiceImpl implements BaoHanhService {
    @Autowired
    private ThongTinBaoHanhRepository thongTinBaoHanhRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<WarrantyDTO> thongTinBaoHanh(String imei, Integer idKH, String phoneNumber) {
        List<ThongTinBaoHanhRepository.WarrantyProjection> projections = thongTinBaoHanhRepository.thongTinBaoHanh(idKH, imei, phoneNumber);

        // Chuyển đổi từ WarrantyProjection sang WarrantyDTO (nếu cần)
        List<WarrantyDTO> dtos = projections.stream()
                .map(p -> {
                    WarrantyDTO dto = new WarrantyDTO();
                    dto.setID(p.getID());
                    dto.setIdKhachHang(p.getIdKhachHang());
                    dto.setThoiGianBH(p.getThoiGianBH());
                    dto.setNgayBD(p.getNgayBD());
                    dto.setTrangThai(p.getTrangThai());
                    dto.setSANPHAMID(p.getSANPHAMID());
                    dto.setTENSP(p.getTENSP());
                    dto.setSdt(p.getSdt());
                    dto.setImei(p.getImei());
                    dto.setReason(p.getReason());
                    return dto;
                })
                .collect(Collectors.toList());
//        return new PageImpl<>(dtos, PageRequest.of(pageNumber, pageSize), dtos.size());
        return dtos;
    }
    @Scheduled(cron = "0 0 10 * * *")
//    @Scheduled(cron = "0 * * * * *")
    public void reportCurrentTime() {
        System.out.println("hello");
        List<HoaDon> hoaDons = hoaDonRepository.findHoaDonByTrangThai(TRANGTHAIDONHANG.THANH_CONG);
        List<HoaDonDTO> hoaDonDTOS = hoaDons.stream()
                .filter(hd -> hd.getHoaDonChiTiets().size()>0)
                .map(f -> modelMapper.map(f, HoaDonDTO.class))
                .collect(Collectors.toList());

        // luu tung thong tin bao hanh
        List<ThongTinBaoHanh> thongTinBaoHanhs = new ArrayList<>();
        for(HoaDonDTO hoaDonDTO : hoaDonDTOS){
            KhachHang khachHang = khachHangRepository.findById(Integer.parseInt(hoaDonDTO.getIdKhachHang())).get();
            List<HoaDonChiTietDTO> hoaDonChiTietDTOS = hoaDonDTO.getHoaDonChiTiets();
            for (HoaDonChiTietDTO hoaDonChiTietDTO : hoaDonChiTietDTOS){
                ThongTinBaoHanh thongTinBaoHanh = new ThongTinBaoHanh();
//                thongTinBaoHanh.setImei(hoaDonChiTietDTO.getImei().getImei());
                thongTinBaoHanh.setIdKhachHang(khachHang);
                thongTinBaoHanh.setNgayBatDau(Instant.now());
                thongTinBaoHanh.setThoiGianBaoHanh(FunctionUtil.getDate(Instant.now(), 2));
                thongTinBaoHanh.setTrangThai("Chưa bảo hành");
                thongTinBaoHanhs.add(thongTinBaoHanh);
            }
        }
        thongTinBaoHanhRepository.saveAll(thongTinBaoHanhs);
    }
}
