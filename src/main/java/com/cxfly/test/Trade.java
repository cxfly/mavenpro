package com.cxfly.test;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Trade implements Serializable {
    /**
     * 
     */
    private static final long   serialVersionUID = 6846084028446759615L;

    private String              name;

    private int                 age;

    private long                id;

    private Map<String, Object> map;

    private Date                gmtCreate;

    private TradeStatus         TradeStatus;

    private List<Order>         orderList;

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Trade() {

    }

    public Trade(String name, int age, Map<String, Object> map, long id) {
        this.name = name;
        this.age = age;
        this.map = map;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public int getAge() {

        return age;

    }

    public void setAge(int age) {

        this.age = age;

    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public TradeStatus getTradeStatus() {
        return TradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        TradeStatus = tradeStatus;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
