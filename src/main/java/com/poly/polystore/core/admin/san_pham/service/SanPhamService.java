package com.poly.polystore.core.admin.san_pham.service;

import com.poly.polystore.core.admin.san_pham.mapper.SanPhamTopRevenue;
import com.poly.polystore.core.admin.san_pham.mapper.SanPhamTopRevenueDTO;
import com.poly.polystore.repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private ModelMapper modelMapper;
    public List<SanPhamTopRevenueDTO> getTopRevenue(){
        List<SanPhamTopRevenue> spdt = sanPhamRepository.topRevenue();
        List<SanPhamTopRevenueDTO> sanPhamTopRevenueDTOS = modelMapper.map(spdt, new TypeToken<List<SanPhamTopRevenueDTO>>() {}.getType());
        sanPhamTopRevenueDTOS.forEach(s -> {
            DecimalFormat formatter = new DecimalFormat("#,###.##");

            String formattedRevenue = formatter.format(s.getDoanhthu()).concat(" VND");
            s.setFormattedRevenue(formattedRevenue);
        });
        return sanPhamTopRevenueDTOS;
    }
}
