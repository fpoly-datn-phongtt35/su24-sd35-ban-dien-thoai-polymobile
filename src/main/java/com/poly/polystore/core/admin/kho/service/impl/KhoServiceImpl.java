package com.poly.polystore.core.admin.kho.service.impl;

import com.poly.polystore.Constant.TRANGTHAIDONHANG;
import com.poly.polystore.core.admin.ban_hang.model.request.HoaDonTaiQuayAddRequest;
import com.poly.polystore.core.admin.kho.model.request.NhapHangRequest;
import com.poly.polystore.core.admin.kho.repository.LichSuKhoRepositoryImpl;
import com.poly.polystore.core.admin.kho.service.IKhoService;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.*;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KhoServiceImpl implements IKhoService {
    private final ModelMapper modelMapper;
    private final LichSuKhoRepositoryImpl lichSuKhoRepositoryImpl;
    private final LichSuKhoRepository lichSuKhoRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;


    @Override
    @Transactional
    public Boolean save(NhapHangRequest req) {

        Map<Integer,Integer> mapSanPhamChiTiet_soLuong=new HashMap<>();
        LichSuKho lsk=new LichSuKho();
        lsk.setTaiKhoan((TaiKhoan) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        lsk.setGhiChu(req.getNote());
        lsk.setThoiGian(Instant.now());

        List<ChiTietLichSuKho> ctlsk= req.getData().stream().map(data->{
            ChiTietLichSuKho chiTietLichSuKho=new ChiTietLichSuKho();
            chiTietLichSuKho.setSanPhamChiTiet(new SanPhamChiTiet(data.getId()));
            chiTietLichSuKho.setImeis(
                    data.getImeis().stream().map(i->{
                        var imei=new Imei();
                        imei.setImei(i);
                        imei.setSanPhamChiTiet(new SanPhamChiTiet(data.getId()));
                        imei.setTrangThai(Imei.TrangThai.TRONG_KHO);
                        return imei;
                    }).collect(Collectors.toList())
            );
            chiTietLichSuKho.setLichSuKho(lsk);
            chiTietLichSuKho.setSoLuong(data.getImeis().size());
            mapSanPhamChiTiet_soLuong.put(data.getId(),data.getImeis().size());
            return chiTietLichSuKho;
        }).collect(Collectors.toList());
        lsk.setChiTietLichSuKhos(ctlsk);
        lichSuKhoRepository.save(lsk);
        
        var spcts=sanPhamChiTietRepository.findAllById(mapSanPhamChiTiet_soLuong.keySet());
        spcts.forEach(item->item.setSoLuong(item.getSoLuong()+mapSanPhamChiTiet_soLuong.get(item.getId())));
        sanPhamChiTietRepository.saveAll(spcts);

        return true;
    }
}
