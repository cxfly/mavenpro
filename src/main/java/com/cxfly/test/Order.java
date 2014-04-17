package com.cxfly.test;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Order implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 849458931535636052L;
    
    
    public Order() {
        super();
        // TODO Auto-generated constructor stub
    }


    
    
    public Order(Long id, String orderNo) {
        super();
        this.id = id;
        this.orderNo = orderNo;
    }




    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    private Long id;
    private String orderNo;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    
    
    
}
