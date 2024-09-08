package com.poly.polystore.core.admin.thong_ke.service.impl;

import com.poly.polystore.core.admin.thong_ke.service.IStatisticService;
import com.poly.polystore.repository.HoaDonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements IStatisticService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Override
    public Map<String, Map<String, BigDecimal>> statisticByMonth() {
        List<Object[]> results = hoaDonRepository.calculateMonthlyRevenue();

        Map<String, Map<String, BigDecimal>> revenueData = new LinkedHashMap<>();

        for (Object[] result : results) {
            Integer monthIndex = (Integer) result[0];
            Integer year = (Integer) result[1];
            BigDecimal totalRevenue = (BigDecimal) result[2];

            String monthName = getMonthName(monthIndex);

            // Tạo hoặc lấy Map<String, BigDecimal> tương ứng với năm hiện tại
            Map<String, BigDecimal> monthlyRevenue = revenueData.getOrDefault(year.toString(),initializeFullYear());

            // Thêm doanh thu theo tháng vào Map
            monthlyRevenue.put(monthName, totalRevenue);

            // Đưa Map đã cập nhật vào Map chính revenueData
            revenueData.put(year.toString(), monthlyRevenue);
        }

        return revenueData;
    }
    private Map<String, BigDecimal> initializeFullYear() {
        Map<String, BigDecimal> fullYear = new LinkedHashMap<>();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        for (String month : months) {
            fullYear.put(month, BigDecimal.ZERO);
        }

        return fullYear;
    }
    private String getMonthName(int monthIndex) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[monthIndex - 1];
    }
}
