package com.cxfly.lp.express;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ReadWlCompanyPropertiesUtil {

    private static Properties properties              = new Properties();
    private static Properties logisticsInfoProperties = new Properties();
    private static Properties matchProperties         = new Properties();
    static {
        try {
            InputStream fileInputStream = ReadWlCompanyPropertiesUtil.class.getClassLoader()
                    .getResourceAsStream("express/logistics_company.properties");
            properties.load(fileInputStream);
            fileInputStream.close();

            InputStream logisticsInfofis = ReadWlCompanyPropertiesUtil.class.getClassLoader()
                    .getResourceAsStream("express/logistics_company_info.properties");
            logisticsInfoProperties.load(logisticsInfofis);
            logisticsInfofis.close();

            InputStream matchfis = ReadWlCompanyPropertiesUtil.class.getClassLoader()
                    .getResourceAsStream("express/province_match.properties");
            matchProperties.load(matchfis);
            matchfis.close();
        } catch (IOException e) {
            throw new RuntimeException("error while init properties helper", e);
        }
    }

    /**
     * 根据物流公司编码获取物流公司taobao的注册ID
     * 
     * @param key
     * @return
     */
    public static String siteMapProperties(String key) {
        String str = "[" + key + "]";
        return properties.getProperty(str);
    }

    /**
     * 根据物流公司编码获取物流公司名称
     * 
     * @param key
     * @return
     */
    public static String logisticsMapProperties(String key) {
        String str = "[" + key + "]";
        return logisticsInfoProperties.getProperty(str);
    }

    /**
     * 获取所有物流公司的信息<br>
     * key是物流公司编码<br>
     * value是物流公司名称
     * 
     * @return
     */
    public static Map<String, String> queryAllLogistics() {
        Map<String, String> result = new HashMap<String, String>();
        Set<Entry<Object, Object>> entrySet = logisticsInfoProperties.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            result.put((String) entry.getKey(), (String) entry.getValue());
        }
        return result;
    }

    /**
     * 根据物流公司编码判断此物流公司是否存在
     * 
     * @param logisticsName
     * @return
     */
    public static boolean contains(String logisticsName) {
        logisticsName = "[" + logisticsName + "]";
        Boolean result = Boolean.FALSE;
        Map<String, String> map = queryAllLogistics();
        Set<String> keySet = map.keySet();
        if (keySet.contains(logisticsName)) {
            result = Boolean.TRUE;
        }
        return result;
    }

    /**
     * 根据外部渠道传入的省份名称匹配自己的系统定义的省份名称
     * 
     * @param inner
     * @param outter
     * @return
     */
    public static Boolean isMatch(String inner, String outter) {
        String matchProvince = findMatchProperties(inner);
        if (StringUtils.contains(matchProvince, outter)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 根据内部系统定义的省份名称获得相应所有外部系统的省份名称，使用逗号隔开
     * 
     * @param key
     * @return
     */
    private static String findMatchProperties(String key) {
        return matchProperties.getProperty(key);
    }
}
