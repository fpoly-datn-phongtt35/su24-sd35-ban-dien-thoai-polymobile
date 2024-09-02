package com.poly.polystore.core.client.service;

import com.poly.polystore.core.client.models.request.WarrantyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaoHanhService {
    List<WarrantyDTO> thongTinBaoHanh(String imei, Long idKH, String phoneNumber);
}
