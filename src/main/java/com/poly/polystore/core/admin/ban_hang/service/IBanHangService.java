package com.poly.polystore.core.admin.ban_hang.service;

import com.poly.polystore.core.admin.ban_hang.model.request.HoaDonTaiQuayAddRequest;
import org.springframework.http.ResponseEntity;

public interface IBanHangService {
    ResponseEntity<?> save(HoaDonTaiQuayAddRequest req);
}
