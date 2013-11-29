package com.alibaba.lp.express;

import java.io.Serializable;
import java.util.Date;

/**
 * @author john
 */
public class LogisticsTraceStep implements  Serializable ,Comparable<LogisticsTraceStep>{

    /**
     * 
     */
    private static final long serialVersionUID = -2175724736595132850L;
    /**
     * 接收地址
     */
    private String            acceptAddress;
    /**
     * 接收时间［东八区］
     */
    private Date              acceptTime;
    /**
     * 备注
     */
    private String            remark;

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setAcceptAddress(String acceptAddress) {
        this.acceptAddress = acceptAddress;
    }

    public String getAcceptAddress() {
        return acceptAddress;
    }

    @Override
    public int compareTo(LogisticsTraceStep o) {
        return this.acceptTime.compareTo(o.getAcceptTime());
    }

}
