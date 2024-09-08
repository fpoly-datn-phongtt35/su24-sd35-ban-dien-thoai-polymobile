package com.poly.polystore.core.client.service.impl;

import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.core.client.service.BaoHanhService;
import com.poly.polystore.repository.ThongTinBaoHanhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaoHanhServiceImpl implements BaoHanhService {
    @Autowired
    private ThongTinBaoHanhRepository thongTinBaoHanhRepository;
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
                    return dto;
                })
                .collect(Collectors.toList());
//        return new PageImpl<>(dtos, PageRequest.of(pageNumber, pageSize), dtos.size());
        return dtos;
    }
}
