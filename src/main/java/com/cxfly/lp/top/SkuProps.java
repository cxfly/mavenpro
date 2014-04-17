package com.cxfly.lp.top;

import java.util.ArrayList;
import java.util.List;

public class SkuProps {
    private final List<SkuProp> skuPropList = new ArrayList<SkuProp>();

    public void add(SkuProp skuProp) {
        skuPropList.add(skuProp);
    }

    public boolean contain(SkuProp skuProp) {
        return skuPropList.contains(skuProp);
    }

    public boolean remove(SkuProp skuProp) {
        return skuPropList.remove(skuProp);
    }

    public List<SkuProp> getSkuPropList() {
        return skuPropList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((skuPropList == null) ? 0 : getListHashCode(skuPropList));
        return result;
    }

    private int getListHashCode(List<SkuProp> propList) {
        int result = 0;
        for (SkuProp skuProp : propList) {
            result += skuProp.hashCode();
        }
        return 31 + result;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SkuProps other = (SkuProps) obj;
        if (skuPropList == null) {
            if (other.skuPropList != null)
                return false;
        } else {
            if (other.skuPropList == null)
                return false;
            if (skuPropList.size() != other.skuPropList.size())
                return false;
            if (!skuPropList.containsAll(other.skuPropList))
                return false;
        }
        return true;
    }

}
