package com.poly.polystore.core.client.convert;

import com.poly.polystore.core.client.models.request.WarrantyDTO;
import com.poly.polystore.repository.ThongTinBaoHanhRepository;
import org.springframework.core.convert.converter.Converter;
public class WarrantyProjectionDtoConverter implements Converter<ThongTinBaoHanhRepository.WarrantyProjection, WarrantyDTO> {

    @Override
    public WarrantyDTO convert(ThongTinBaoHanhRepository.WarrantyProjection projection) {
        WarrantyDTO dto = new WarrantyDTO();
        dto.setID(projection.getID());
        dto.setIdKhachHang(projection.getIdKhachHang());
        dto.setThoiGianBH(projection.getThoiGianBH());
        dto.setNgayBD(projection.getNgayBD());
        dto.setTrangThai(projection.getTrangThai());
        dto.setSANPHAMID(projection.getSANPHAMID());
        dto.setTENSP(projection.getTENSP());
        return dto;
    }

}