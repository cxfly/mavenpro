/**
 * Project: lp4pl.biz.logistics
 * 
 * File Created at 2012-7-16
 * $Id$
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.lp.express.excel;

/**
 * 分页查询回调接口
 * 
 * @author wb_zhe.liz 2012-7-16 下午6:36:36
 */
public interface PageQueryCallback {

    /** 默认的分页大小 */
    public static final int DEFAULT_PAGE_SIZE = 10000;

    /**
     * 分页查询回调接口
     * 
     * @param pageNo
     * @return
     */
    Page<?> getPage(int pageNo);
}
