package com.poly.polystore.core.client.service;

import com.poly.polystore.core.client.dto.HoaDonDTO;
import com.poly.polystore.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IOrderService {
    List<HoaDonDTO> findOrderByUser(Authentication authentication, String startDate, String endDate, String status, String maDH);
    Page<HoaDon> findOrderByUserPage(Authentication authentication, String startDate, String endDate, String status, String maDH, int page, int size);
    HoaDonDTO findOrderById(Integer orderId);
}
