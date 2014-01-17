package com.cxfly.test.xstream;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("trade")
public class Trade {
    @JSONField(name = "trade_no")
    @XStreamAlias("trade_no")
    private String      tradeNo;

    @JSONField(name = "orders")
    @XStreamAlias("orders")
    private List<Order> orderList;

    @XStreamAlias("address")
    private Address     address;

    @XStreamAlias("address")
    @XStreamImplicit(itemFieldName = "item")
    private String[]    itemList;

    public String[] getItemList() {
        return itemList;
    }

    public void setItemList(String[] itemList) {
        this.itemList = itemList;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Trade [tradeNo=" + tradeNo + ", orderList=" + orderList + ", address=" + address
                + "]";
    }

}
