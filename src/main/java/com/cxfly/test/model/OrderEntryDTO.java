package com.cxfly.test.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 类OrderEntry. 订单信息
 * 
 * @author fei.dengf 2011-8-4 下午2:57:34
 */
public class OrderEntryDTO {

    private String                             wareHouseCode;
    private String                             orderCode;
    private String                             orderType;
    private ReceiverDTO                        receiver;
    private final Map<String, List<OrderItem>> items = new HashMap<String, List<OrderItem>>();
    private String                             remarks;

    public String getWareHouseCode() {
        return wareHouseCode;
    }

    public void setWareHouseCode(String wareHouseCode) {
        this.wareHouseCode = wareHouseCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public ReceiverDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverDTO receiver) {
        this.receiver = receiver;
    }

    public Map<String, List<OrderItem>> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items.put("item", items);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
