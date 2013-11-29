package com.alibaba.lp.top;

/**
 * 类SkuProperty.java的实现描述：TODO 类实现描述
 * 
 * @author wb_guanghui.zhugh 2011-8-9 下午03:17:38
 */
public class SkuProp {

    private String type;
    private String value;

    public SkuProp(String type, String value){
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SkuProp other = (SkuProp) obj;
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
    
}
