package com.cxfly.test;

public enum TradeStatus {
    NEW(0, "已创建"),
    PAID(1, "已付款"),
    SENT(2, "已发货");

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    private final  int    val;
    private final String desc;

    private TradeStatus(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
}
