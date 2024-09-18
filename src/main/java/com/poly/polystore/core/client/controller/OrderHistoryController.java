package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.client.dto.HoaDonDTO;
import com.poly.polystore.core.client.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class OrderHistoryController {
    private final IOrderService iOrderService;

    @GetMapping("/don-mua")
    public String getOrderHistory(@RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate,
                                  @RequestParam(value = "maDH", required = false) String maDH,
                                  Authentication authentication,
                                  Model model) {
        List<HoaDonDTO> hoaDonDTOS = iOrderService.findOrderByUser(authentication, startDate, endDate, status, maDH);
        model.addAttribute("orders", hoaDonDTOS);
        return "/client/page/donmua";
    }

    @GetMapping("/don-muav2")
    public String getOrderHistoryV2(@RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "startDate", required = false) String startDate,
                                    @RequestParam(value = "endDate", required = false) String endDate,
                                    @RequestParam(value = "maDH", required = false) String maDH,
                                    Authentication authentication,
                                    Model model) {
        List<HoaDonDTO> hoaDonDTOS = iOrderService.findOrderByUser(authentication, startDate, endDate, status, maDH);
        model.addAttribute("orders", hoaDonDTOS);
        return "/client/page/donmua:: #orderList";
    }
}
