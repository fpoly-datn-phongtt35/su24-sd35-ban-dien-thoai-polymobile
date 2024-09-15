package com.poly.polystore.Constant;

import java.util.stream.Stream;

public enum TRANGTHAIDONHANG {
    CHO_THANH_TOAN("Chờ Thanh Toán"),
    CHO_XAC_NHAN("Chờ xác nhận"),
    DANG_GIAO("Đang Giao"),
    THANH_CONG("Giao Thành Công"),
    DA_HUY("Đơn hàng bị hủy"),
    MOI("Mới"),
    XAC_NHAN("Xác nhận"),
    DANG_CHUAN_BI_HANG("Đang chuẩn bị hàng"),
    CHO_LAY_HANG("Chờ lấy hàng"),
    LAY_HANG_THANH_CONG("Lấy hàng thành công"),
    DANG_VAN_CHUYEN("Đang vận chuyển"),
    GIAO_HANG_THANH_CONG("Giao hàng thành công"),
    GIAO_HANG_THAT_BAI("Giao hàng thất bại"),
    CHO_CHUYEN_HOAN("Chờ chuyển hoàn"),
    THAT_LAC("Thất lạc"),
    XAC_NHAN_HOAN_KHO("Xác nhận hoàn kho"),
    XAC_NHAN_HOAN_KHO_MOT_PHAN("Xác nhận hoàn kho một phần");

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
    public static TRANGTHAIDONHANG fromString(String status) {
        if (status == null || status.equals("ALL")) {
            return null;  // Giá trị "ALL" tương đương với không có bộ lọc
        }
        try {
            return TRANGTHAIDONHANG.valueOf(status);
        } catch (IllegalArgumentException e) {
            return null;  // Nếu giá trị không hợp lệ
        }
    }
}
