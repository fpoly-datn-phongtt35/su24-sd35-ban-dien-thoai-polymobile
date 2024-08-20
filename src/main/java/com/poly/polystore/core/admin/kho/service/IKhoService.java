package com.poly.polystore.core.admin.kho.service;

import com.poly.polystore.core.admin.ban_hang.model.request.HoaDonTaiQuayAddRequest;
import com.poly.polystore.core.admin.kho.model.request.NhapHangRequest;
import com.poly.polystore.entity.LichSuKho;
import org.springframework.http.ResponseEntity;

public interface IKhoService {
    Boolean save(NhapHangRequest req);
}
