/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.alibaba.lp.top;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.api.domain.Item;

/**
 * 类TaoBaoFullItemInfo.java的实现描述：淘宝完整item类，包括item信息和扩展的sku信息
 * 
 * @author tom.yuy 2011-11-9 下午07:43:28
 */
public class TaoBaoFullItemInfo {

    private Item                  item;

    private List<TaoBaoSkuExtend> taoBaoSkuExtends;
    
    // 自定义属性与销售属性重复定义
    private String repeatedSkuProp;
    
    // 商品的销售属性,以自定义为准
    private String productSkuDefinition;
    
    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return the taoBaoSkuExtends
     */
    public List<TaoBaoSkuExtend> getTaoBaoSkuExtends() {
        return taoBaoSkuExtends;
    }

    /**
     * @param taoBaoSkuExtends the taoBaoSkuExtends to set
     */
    public void setTaoBaoSkuExtends(List<TaoBaoSkuExtend> taoBaoSkuExtends) {
        this.taoBaoSkuExtends = taoBaoSkuExtends;
    }

    public String getRepeatedSkuProp() {
        return repeatedSkuProp;
    }

    public void setRepeatedSkuProp(String repeatedSkuProp) {
        this.repeatedSkuProp = repeatedSkuProp;
    }
    
    public boolean isExistsRepeatdProps() {
        if (repeatedSkuProp!=null && repeatedSkuProp.trim().length()>0) {
            return true;
        }
        return false;
    }

    public String getProductSkuDefinition() {
        return productSkuDefinition;
    }

    public void setProductSkuDefinition(String productSkuDefinition) {
        this.productSkuDefinition = productSkuDefinition;
    }

    public  Map<Long, TaoBaoSkuExtend> getTaoBaoSkuExtendsMap() {
        Map<Long, TaoBaoSkuExtend> result = new HashMap<Long, TaoBaoSkuExtend>();
        if (taoBaoSkuExtends!=null && taoBaoSkuExtends.size()>0) {
            for (TaoBaoSkuExtend item : taoBaoSkuExtends) {
                result.put(item.getSkuId(), item);
            }
        }
        return result;
    }

}
