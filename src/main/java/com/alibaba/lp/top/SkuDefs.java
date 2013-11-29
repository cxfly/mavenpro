package com.alibaba.lp.top;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 类SkuDefs.java的实现描述：TODO 类实现描述 
 * @author wb_guanghui.zhugh 2011-8-9 下午04:43:15
 */
public class SkuDefs implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<SkuDef> skuDefList = new ArrayList<SkuDef>();
    
    public void add(SkuDef skuDef) {
        skuDefList.add(skuDef);
    }
    
    public boolean contain(SkuDef skuDef) {
        return skuDefList.contains(skuDef);
    }
    
    public boolean remove(SkuDef skuDef) {
        return skuDefList.remove(skuDef);
    }

    public SkuDef get(String type) {
        for (SkuDef def : skuDefList) {
            if (def.getType().equals(type)) {
                return def;
            }
        }
        return null;
    }
    
    public List<SkuDef> getSkuDefList() {
        return skuDefList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((skuDefList == null) ? 0 : getListHashCode(skuDefList));
        return result;
    }
    
    private int getListHashCode(List<SkuDef> defList) {
        int result = 0;
        for (SkuDef skuDef : defList) {
            result += skuDef.hashCode();
        }
        return 31 + result;
        
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SkuDefs other = (SkuDefs) obj;
        if (skuDefList == null) {
            if (other.skuDefList != null) return false;
        } else {
            if (other.skuDefList == null) return false;
            if (skuDefList.size() != other.skuDefList.size()) return false;
            if (!skuDefList.containsAll(other.skuDefList)) return false;
        }
        return true;
    }
    
    
}
