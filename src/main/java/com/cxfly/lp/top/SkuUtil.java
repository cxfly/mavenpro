package com.cxfly.lp.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

public class SkuUtil {

    public static final String SKU_SEPERATOR            = ";";
    public static final String SKU_TYPE_VALUE_SEPERATOR = ":";
    public static final String SKU_VALUE_SEPERATOR      = ",";

    /**
     * 将SKU定义字符形式解析为SKU定义对象。 要求参数不能为空，如果格式错误返回null
     * 
     * @param skuDefsStr 格式如SIZE:S,M,L;颜色:红,白
     * @return
     */
    public static SkuDefs parseSkuDefs(String skuDefsStr) {
        if (StringUtils.isEmpty(skuDefsStr)) {
            throw new IllegalArgumentException("skuDefsStr should not be empty!");
        }
        SkuDefs skuDefs = new SkuDefs();
        String[] skuDefArr = skuDefsStr.split(SKU_SEPERATOR);
        for (String skuDefStr : skuDefArr) {
            String[] skuArr = skuDefStr.split(SKU_TYPE_VALUE_SEPERATOR);
            if (skuArr == null || skuArr.length != 2) {
                return null;
            }
            String type = skuArr[0];
            String[] values = skuArr[1].split(SKU_VALUE_SEPERATOR);

            SkuDef skuDef = new SkuDef(type, Arrays.asList(values));
            skuDefs.add(skuDef);
        }
        return skuDefs;
    }

    /**
     * 将SKU定义格式化成字符形式（格式如SIZE:S,M,L;颜色:红,白）
     * 
     * @param skuDefs
     * @return
     */
    public static String formatSkuDefs(SkuDefs skuDefs) {
        if (skuDefs == null) {
            throw new IllegalArgumentException("skuDefs must not be null");
        }
        StringBuilder sb = new StringBuilder();
        List<SkuDef> defList = skuDefs.getSkuDefList();

        int size = 0;
        for (SkuDef def : defList) {
            if ((def.getValueList() != null && def.getValueList().size() > 0)
                    || (def.getNewValueList() != null && def.getNewValueList().size() > 0)) {
                sb.append(def.getType()).append(SKU_TYPE_VALUE_SEPERATOR);
                for (String val : def.getValueList()) {
                    sb.append(val).append(SKU_VALUE_SEPERATOR);
                }
                for (String val : def.getNewValueList()) {
                    sb.append(val).append(SKU_VALUE_SEPERATOR);
                }
                //
                sb.delete(sb.length() - SKU_VALUE_SEPERATOR.length(), sb.length());
                sb.append(SKU_SEPERATOR);
                size++;
            }
        }
        if (size > 1) {
            sb.delete(sb.length() - SKU_SEPERATOR.length(), sb.length());
        }
        return sb.toString();
    }

    /**
     * 将SKU属性字符解释为SKU属性对象 要求参数不能为空，如果格式错误返回null
     * 
     * @param skuPropsStr 格式如SIZE:S;颜色:红
     * @return
     */
    public static SkuProps parseSkuProps(String skuPropsStr) {
        if (StringUtils.isEmpty(skuPropsStr)) {
            throw new IllegalArgumentException("skuDefsStr should not be empty!");
        }
        SkuProps skuProps = new SkuProps();
        String[] skuPropArr = skuPropsStr.split(SKU_SEPERATOR);
        for (String skuPropStr : skuPropArr) {
            String[] skuArr = skuPropStr.split(SKU_TYPE_VALUE_SEPERATOR);
            if (skuArr == null || skuArr.length != 2) {
                return null;
            }

            SkuProp skuProp = new SkuProp(skuArr[0], skuArr[1]);
            skuProps.add(skuProp);
        }
        return skuProps;
    }

    /**
     * 判断这个SKU属性是否在SKU定义里
     * 
     * @param props
     * @param skuDefs
     * @return
     */
    public static boolean isBelongToDef(SkuProps props, SkuDefs skuDefs) {
        Map<String, List<String>> defMap = getSkuDefMap(skuDefs);
        for (SkuProp prop : props.getSkuPropList()) {
            List<String> valueList = defMap.get(prop.getType());
            if (valueList == null || !valueList.contains(prop.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Map形式的SKU定义 如key:颜色 value：[红色,黄色]
     * 
     * @param skuDefs
     * @return
     */
    public static Map<String, List<String>> getSkuDefMap(SkuDefs skuDefs) {
        Map<String, List<String>> defMap = new HashMap<String, List<String>>();
        List<SkuDef> defList = skuDefs.getSkuDefList();
        for (SkuDef def : defList) {
            defMap.put(def.getType(), def.getValueList());
        }
        return defMap;
    }

    /**
     * 将skuProps中的中文全角标点转换为英文标点
     * 
     * @param skuProps
     * @return
     */
    public static String formatSkuProps(String skuProps) {
        if (skuProps == null) {
            return null;
        }
        String formateProps = skuProps.replace("；", SKU_SEPERATOR);
        formateProps = formateProps.replace("：", SKU_TYPE_VALUE_SEPERATOR);
        return formateProps;
    }

    /**
     * 比对SKU属性是否全部一致
     * 
     * @param sku1
     * @param sku2
     * @return
     */
    public static boolean isSkuEqual(String sku1, String sku2) {

        boolean isMapper1 = isSkuPropertyEqual(sku1, sku2);
        boolean isMapper2 = isSkuPropertyEqual(sku2, sku1);
        if (isMapper1 && isMapper2) {
            return true;
        }

        return false;
    }

    /**
     * 比对SKU属性是否全部一致
     * 
     * @param sku1
     * @param sku2
     * @return
     */
    private static boolean isSkuPropertyEqual(String sku1, String sku2) {

        if (StringUtils.isBlank(sku1) || StringUtils.isBlank(sku2)) {
            return StringUtils.equals(sku1, sku2);
        }

        // sku属性肯定不为空，因为前面有验证
        String skuproperties1[] = sku1.split(";");
        List<String> skupropertieList1 = new ArrayList<String>(Arrays.asList(skuproperties1));
        boolean isMapper = true;
        for (String str1 : skupropertieList1) {
            String[] keyValues = str1.split(":");
            if (keyValues != null && keyValues.length > 1) {
                if (!sku2.contains(keyValues[1]) && !sku2.contains(keyValues[1].trim())) {
                    isMapper = false;
                    break;
                }
            }
        }
        return isMapper;
    }

    /**
     * 将repeatedPropSet集合元素连接成一个字符串 repeatedPropSet
     * 
     * @param repeatedPropSet
     * @return　重复的sku属性(格式如： [颜色分类: 紫色],[颜色分类: 紫色] )
     */
    public static String joinRepeatedPropsStr(Set<KeyNamePair<String, String>> repeatedPropSet) {
        String result = "";
        if (CollectionUtils.isEmpty(repeatedPropSet)) {
            return result;
        }
        StringBuilder buf = new StringBuilder();
        for (KeyNamePair<String, String> keyNamePair : repeatedPropSet) {
            buf.append(SKU_VALUE_SEPERATOR).append(keyNamePair);
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(0);
        }
        result = buf.toString();
        return result;
    }

    /**
     * 将skuKeyMap中的元素连接成字符串
     * 
     * @param skuKeyGroupMap
     * @return 商品属性字符串(格式如：尺码:36(3.11尺),40(3.20尺);颜色:紫色,蓝色,紫色,浅绿色)
     */
    public static String joinSkuKeyGroupMap2Str(Map<String, List<String>> skuKeyGroupMap) {
        String result = "";
        if (CollectionUtils.isEmpty(skuKeyGroupMap)) {
            return result;
        }

        StringBuilder buf = new StringBuilder();
        for (String key : skuKeyGroupMap.keySet()) {
            buf.append(SKU_SEPERATOR).append(key).append(SKU_TYPE_VALUE_SEPERATOR);
            List<String> propValue = skuKeyGroupMap.get(key);
            for (int i = 0; i < propValue.size(); i++) {
                if (i > 0) {
                    buf.append(SKU_VALUE_SEPERATOR);
                }
                buf.append(propValue.get(i));
            }
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(0);
        }
        result = buf.toString();

        return result;
    }

    /**
     * 将keyNamePairList中的元素用指定的分隔符连接成sku属性字符串
     * 
     * @param keyNamePairList
     * @param split
     * @return sku属性字符串(格式如： 颜色:天蓝色;尺码:36)
     */
    public static String joinKeyNamePair2Str(List<KeyNamePair<String, String>> keyNamePairList,
                                             String split) {
        String result = "";
        if (CollectionUtils.isEmpty(keyNamePairList)) {
            return result;
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < keyNamePairList.size(); i++) {
            KeyNamePair<String, String> keyNamePair = keyNamePairList.get(i);
            if (i > 0) {
                buf.append(split);
            }
            buf.append(keyNamePair.getKey()).append(SKU_TYPE_VALUE_SEPERATOR)
                    .append(keyNamePair.getName());
        }
        result = buf.toString();

        return result;
    }

    /**
     * 将商品属性名称解析为一个map对象返回
     * 
     * @param propsName
     *            ，格式如：1627741:3267175:裤长:短裤（穿起至膝盖以上）;1632501:149576550:货号:
     *            AW12125010115
     * @return Map<String, KeyNamePair<String, String>, key="1627741:3267175",
     *         value=KeyNamePair<"货号","AW12125010115">
     */
    public static Map<String, KeyNamePair<String, String>> parsePropsName2Map(String propsName) {
        Map<String, KeyNamePair<String, String>> result = new HashMap<String, KeyNamePair<String, String>>();
        if (StringUtils.isBlank(propsName)) {
            return result;
        }

        String[] propsNames = propsName.split(SKU_SEPERATOR);
        String key = null;
        KeyNamePair<String, String> pair = null;
        for (int i = 0; i < propsNames.length; i++) {
            String[] props = propsNames[i].split(SKU_TYPE_VALUE_SEPERATOR);
            if (props.length != 4) {
                continue;
            }

            key = new StringBuffer().append(props[0]).append(SKU_TYPE_VALUE_SEPERATOR)
                    .append(props[1]).toString();
            pair = new KeyNamePair<String, String>(props[2], props[3]);
            result.put(key, pair);
        }
        return result;
    }

    /**
     * 将一组sku自定义属性解析为Map对象
     * 
     * @param propertyAlias (格式如:20506:3271773:40（3.06尺）;1627207:3232483:牛仔蓝)
     * @return Map<String,String> 格式： key="1627207:3232483"; value="牛仔蓝"
     */
    public static Map<String, String> parsePropertyAlias2Map(String propertyAlias) {
        Map<String, String> idCpropMap = new HashMap<String, String>();
        if (!StringUtils.isBlank(propertyAlias)) {
            // 1627207:3232482:测试绿;1627207:3232484:测试蓝
            // 1627207:3232482:测试绿
            String[] idCpropList = propertyAlias.split(SKU_SEPERATOR);

            for (int i = 0; i < idCpropList.length; i++) {
                String idCprop = idCpropList[i];// 1627207:3232482:测试绿
                int pidSite = idCprop.indexOf(SKU_TYPE_VALUE_SEPERATOR);
                if (pidSite > -1) {
                    String pid = idCprop.substring(0, pidSite);// 1627207:3232482
                    String vidCprop = idCprop.substring(pidSite + 1, idCprop.length());
                    int vidSite = vidCprop.indexOf(SKU_TYPE_VALUE_SEPERATOR);
                    if (vidSite > -1) {
                        String vid = vidCprop.substring(0, vidSite);
                        String cprop = vidCprop.substring(vidSite + 1, vidCprop.length());// 测试绿
                        String id = pid + SKU_TYPE_VALUE_SEPERATOR + vid;
                        if (!StringUtils.isBlank(id) && !StringUtils.isBlank(cprop)) {
                            idCpropMap.put(id, cprop);
                        }
                    }
                }
            }
        }
        return idCpropMap;
    }

}
