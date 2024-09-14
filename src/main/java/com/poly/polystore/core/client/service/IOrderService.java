package com.poly.polystore.core.client.service;

import com.poly.polystore.core.client.dto.HoaDonDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IOrderService {
    List<HoaDonDTO> findOrderByUser(Authentication authentication, String startDate, String endDate, String status, String maDH);
    HoaDonDTO findOrderById(Integer orderId);
}
