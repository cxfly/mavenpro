package com.cxfly.test.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("order")
public class Order {
    private String name;

    @JSONField(name = "buy_num")
    @XStreamAlias("buy_num")
    private int    buyNum;

    public Order() {
        super();
    }

    public Order(String name, int buyNum) {
        super();
        this.name = name;
        this.buyNum = buyNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    @Override
    public String toString() {
        return "Order [name=" + name + ", buyNum=" + buyNum + "]";
    }

}
