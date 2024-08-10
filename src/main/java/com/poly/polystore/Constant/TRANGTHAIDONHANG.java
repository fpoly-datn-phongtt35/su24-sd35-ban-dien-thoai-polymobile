package com.poly.polystore.Constant;

import java.util.stream.Stream;

public enum TRANGTHAIDONHANG {
    CHO_THANH_TOAN("Chờ Thanh Toán"),
    CHO_XAC_NHAN("Chờ xác nhận"),
    DANG_GIAO("Đang Giao"),
    THANH_CONG("Giao Thành Công"),
    DA_HUY("Đơn hàng bị hủy");
    private String priority;

    private TRANGTHAIDONHANG(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    public static TRANGTHAIDONHANG of(String priority) {
        return Stream.of(TRANGTHAIDONHANG.values())
                .filter(p -> p.getPriority().equals(priority))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
