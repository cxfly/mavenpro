package com.alibaba.lp.top;

/**
 * @author tom.yuy
 * 继承自淘宝的Sku类，封装一些淘宝sku类中没有提供的属性
 */

import com.taobao.api.domain.Sku;


public class TaoBaoSkuExtend extends Sku{
    private static final long serialVersionUID = 2126022818372136865L;
    private String propertiesValue;   //属性描述(销售属性)
    private String customPropertiesValue;   //商家自定义属性描述（因自定义属性只是具体到一个pid:vid，若有多个类目的话，无法完整描述一个sku属性）
    private String propertiesWithCustomProperties;//一个sku的属性完整描述，如果有商家自定义属性，则优先取自定义属性（解决上个属性不完整带来的问题）
    
    /**
     * @return the propertiesValue
     */
    public String getPropertiesValue() {
        return propertiesValue;
    }

    /**
     * @param propertiesValue the propertiesValue to set
     */
    public void setPropertiesValue(String propertiesValue) {
        this.propertiesValue = propertiesValue;
    } 
    
    /**
     * @return the customPropertiesValue
     */
    public String getCustomPropertiesValue() {
        return customPropertiesValue;
    }

    /**
     * @param customPropertiesValue the customPropertiesValue to set
     */
    public void setCustomPropertiesValue(String customPropertiesValue) {
        this.customPropertiesValue = customPropertiesValue;
    }

    /**
     * @return the propertiesWithCustomProperties
     */
    public String getPropertiesWithCustomProperties() {
        return propertiesWithCustomProperties;
    }

    /**
     * @param propertiesWithCustomProperties the propertiesWithCustomProperties to set
     */
    public void setPropertiesWithCustomProperties(String propertiesWithCustomProperties) {
        this.propertiesWithCustomProperties = propertiesWithCustomProperties;
    } 
    
    
}
