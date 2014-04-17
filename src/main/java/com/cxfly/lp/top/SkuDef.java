package com.cxfly.lp.top;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkuDef implements Serializable {

    private static final long  serialVersionUID = -3781123393800214599L;
    private final String       type;
    private List<String>       valueList;
    private final List<String> newValueList     = new ArrayList<String>();

    public SkuDef(String type, List<String> valueList) {
        this.type = type;
        this.valueList = valueList;
    }

    public String getType() {
        return type;
    }

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }

    public boolean addNewValue(String value) {
        if (valueList.contains(value) || newValueList.contains(value)) {
            return false;
        }
        newValueList.add(value);
        return true;
    }

    public List<String> getNewValueList() {
        return newValueList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((valueList == null) ? 0 : valueList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SkuDef other = (SkuDef) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (valueList == null) {
            if (other.valueList != null)
                return false;
        } else {
            if (other.valueList == null)
                return false;
            if (valueList.size() != other.valueList.size())
                return false;
            if (!valueList.containsAll(other.valueList))
                return false;
        }
        return true;
    }

    public boolean isNew() {
        if (valueList == null || valueList.size() == 0) {
            return true;
        }
        return false;
    }

}
