/**
 * Project: lp4pl.biz.logistics
 * 
 * File Created at 2013-1-6下午3:01:42
 * $Id$
 * 
 * Copyright 1999-2013 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.test;

/**
 * @author zhengxing 2013-1-6下午3:01:42
 */
public enum LogAction {
    ENTER_WAREHOUSE(1, "入库"),
    OUT_WAREHOUSE(2, "出库"),
    ORDER_FREEZE(3, "订单冻结"),
    ORDER_REFUND(4, "订单取消"),
    ACCEPT_ORDER_FAILED(5, "接单失败"),
    DELIVERY_FAILED(6, "发货失败"),
    DELIVERY(7, "订单发货"),
    CHECK(8, "盘点"),
    ORDER_RELATE(9, "订单关联"),
    //    ORDER_SPLIT(10, "订单拆单"),
    MANUAL_RELEASE(11, "查看库存/手工释放库存"),
    MANUAL_STOCKOUT_SUBMIT_RELEASE(12, "出库单提交/手工释放库存");

    private Integer action;
    private String  desc;

    LogAction(Integer action, String desc) {
        this.action = action;
        this.desc = desc;
    }

    public int getValue() {
        return this.action;
    }

    public String getDesc() {
        return this.desc;
    }

    public static String getDesc(int value) {
        LogAction action = get(value);
        return action.getDesc();
    }

    public static LogAction get(int value) {
        for (LogAction ss : values()) {
            if (ss.action == value) {
                return ss;
            }
        }
        throw new IllegalArgumentException("unknow LogAction value of " + value);
    }

    public boolean isCheck() {
        return this == CHECK;
    }
}
