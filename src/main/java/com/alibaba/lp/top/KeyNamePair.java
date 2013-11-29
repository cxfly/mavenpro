/**
 * Project: lp4pl.biz.logistics
 * 
 * File Created at 2012-6-13
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
package com.alibaba.lp.top;

import java.io.Serializable;

/**
 * KeyNamePair
 * 
 * @author wb_zhe.liz 2012-6-13 下午5:23:14
 * @param <K>主键
 * @param <V>名称
 */
public class KeyNamePair<K, V> implements Serializable {
    private static final long serialVersionUID = -8146348487493102025L;

    private K                 key;
    private V                 name;

    public KeyNamePair() {
        super();
    }

    public KeyNamePair(K key, V name) {
        super();
        this.key = key;
        this.name = name;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getName() {
        return name;
    }

    public void setName(V name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KeyNamePair other = (KeyNamePair) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append(this.key);
        sb.append(": ");
        sb.append(this.name);
        sb.append("]");
        return (sb.toString());
    }

}
