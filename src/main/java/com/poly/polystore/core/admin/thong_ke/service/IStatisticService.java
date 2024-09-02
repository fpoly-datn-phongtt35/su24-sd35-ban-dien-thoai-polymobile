package com.poly.polystore.core.admin.thong_ke.service;

import java.math.BigDecimal;
import java.util.Map;

public interface IStatisticService {
    Map<String, Map<String, BigDecimal>> statisticByMonth();
}
