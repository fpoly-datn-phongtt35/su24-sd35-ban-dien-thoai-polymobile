package com.poly.polystore.Constant;

import java.util.stream.Stream;

public enum TRANGTHAIDONHANG {
    CHO_THANH_TOAN(0),
    CHO_XAC_NHAN(1),
    DANG_GIAO(2),
    THANH_CONG(3),
    DA_HUY(4);
    private int priority;

    private TRANGTHAIDONHANG(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static TRANGTHAIDONHANG of(int priority) {
        return Stream.of(TRANGTHAIDONHANG.values())
                .filter(p -> p.getPriority() == priority)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
