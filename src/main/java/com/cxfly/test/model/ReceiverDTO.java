/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.cxfly.test.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 类ReciverDTO. 订单接受者信息
 * 
 * @author fei.dengf 2011-8-8 下午2:30:41
 */

public class ReceiverDTO {

    private String name;
    private String postCode;
    private String phone;
    private String mobile;
    private String prov;
    private String city;
    private String district;
    private String address;
    private String propDistribution;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPropDistribution() {
        return propDistribution;
    }

    public void setPropDistribution(String propDistribution) {
        this.propDistribution = propDistribution;
    }

    public boolean validateContactWay(String s) {
        if ((this.mobile == null || "".equals(this.mobile))
                && (this.phone == null || "".equals(this.phone))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
