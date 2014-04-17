package com.cxfly.lp.top.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.cxfly.lp.top.KeyNamePair;
import com.cxfly.lp.top.SkuUtil;
import com.cxfly.lp.top.TaoBaoFullItemInfo;
import com.cxfly.lp.top.TaoBaoSkuExtend;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.response.ItemGetResponse;

public class TopService {

    private static final Logger logger            = LoggerFactory.getLogger(TopService.class);
    private static final String PROPERTYS_SPLITER = ";";

    private static final String serviceUrl        = "http://gw.api.taobao.com/router/rest";
    private static final String appKey            = "12349228";
    private static final String appSecret         = "19f329ca748caa251c7524791dc92c7c";
    private final TaobaoClient  taobaoTopClient;

    private static final String sessionKey        = "6102510dafb7afc8841099cb7b0972147107f3875eb6a07908372666";

    public TopService() {
        taobaoTopClient = new DefaultTaobaoClient(serviceUrl, appKey, appSecret);
    }

    public TaoBaoFullItemInfo getSkuList(long itemId) {
        ItemGetRequest req = new ItemGetRequest();
        req.setFields("sku,cid,property_alias,nick,title,price,modified,props,props_name,num_iid,detail_url,num,location");
        req.setNumIid(itemId);

        ItemGetResponse response = null;
        try {
            response = taobaoTopClient.execute(req, sessionKey);
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        if (response == null || !response.isSuccess() || response.getItem() == null) {
            logger.error("can't retrieve sku list with itemId:" + itemId);
            return null;
        }

        Item item = response.getItem();
        TaoBaoFullItemInfo taoBaoFullItemInfo = new TaoBaoFullItemInfo();
        taoBaoFullItemInfo.setItem(item);

        List<Sku> skus = item.getSkus();
        if (CollectionUtils.isEmpty(skus)) {
            return taoBaoFullItemInfo;
        }

        // 拆分SKU属性字符串为Map对象,以便后续程序处理时使用
        Map<String, String> custSkuPropMap = SkuUtil
                .parsePropertyAlias2Map(item.getPropertyAlias());
        Map<String, KeyNamePair<String, String>> propsNameMap = SkuUtil.parsePropsName2Map(item
                .getPropsName());

        String skuPropStr = null; // sku属性原始字符串，格式为1627207:3232483;20506:3217381
        String custPropValueStr = null; // sku自定义属性值, 格式为 "牛仔蓝"
        KeyNamePair<String, String> keyNamePair = null; // sku属性的最终KeyNamePair, 经过自定义属性替换.

        // 临时变量, SKU属性集合,最后会将集合中的元素拼接成一个字符串
        List<KeyNamePair<String, String>> propWithCustPropKeyPairList = new ArrayList<KeyNamePair<String, String>>();
        List<KeyNamePair<String, String>> propKeyPairList = new ArrayList<KeyNamePair<String, String>>();
        List<KeyNamePair<String, String>> custPropKeyPairList = new ArrayList<KeyNamePair<String, String>>();

        List<TaoBaoSkuExtend> taoBaoSkuExtendList = new ArrayList<TaoBaoSkuExtend>();

        // SKU属性缓存,将sku列表中的每个sku属性id(pid:vid)作为key， 对应的最终属性值(颜色:蓝色)作为Value缓存起来到map中
        Map<String, KeyNamePair<String, String>> propsNameCache = new TreeMap<String, KeyNamePair<String, String>>();

        // 开始处理每个Sku
        for (Sku sku : skus) {
            TaoBaoSkuExtend taoBaoSkuExtend = new TaoBaoSkuExtend();
            BeanUtils.copyProperties(sku, taoBaoSkuExtend);
            skuPropStr = sku.getProperties();
            String[] skuPropArray = skuPropStr.split(PROPERTYS_SPLITER); // skuPropStr用分号分隔后的字符串数组
            for (int i = 0; i < skuPropArray.length; i++) {
                keyNamePair = propsNameMap.get(skuPropArray[i]);
                if (keyNamePair != null) {
                    KeyNamePair<String, String> propsValuesKeyPair = new KeyNamePair<String, String>();
                    BeanUtils.copyProperties(keyNamePair, propsValuesKeyPair);
                    propKeyPairList.add(propsValuesKeyPair); // 记录商品属性

                    // 取pid:vid对应的sku自定义属性值
                    custPropValueStr = custSkuPropMap.get(skuPropArray[i]);
                    if (StringUtils.isNotBlank(custPropValueStr)) {
                        keyNamePair.setName(custPropValueStr);
                        custPropKeyPairList.add(keyNamePair); // 仅记录自定义属性
                    }

                    propWithCustPropKeyPairList.add(keyNamePair); // 最终属性值(存在自定义属性，以自定义属性位置，否则以sku商品属性为准)

                    // 缓存最终的sku属性值, 相同的key(pid:vid)只记录一次
                    if (!propsNameCache.containsKey(skuPropArray[i])) {
                        propsNameCache.put(skuPropArray[i], keyNamePair);
                    }
                }
            }

            // keyNamePair List 转化为 sku属性字符串, 用分号连接
            taoBaoSkuExtend.setPropertiesValue(SkuUtil.joinKeyNamePair2Str(propKeyPairList,
                    PROPERTYS_SPLITER));
            taoBaoSkuExtend.setCustomPropertiesValue(SkuUtil.joinKeyNamePair2Str(
                    custPropKeyPairList, PROPERTYS_SPLITER));
            taoBaoSkuExtend.setPropertiesWithCustomProperties(SkuUtil.joinKeyNamePair2Str(
                    propWithCustPropKeyPairList, PROPERTYS_SPLITER));
            taoBaoSkuExtendList.add(taoBaoSkuExtend);

            // 清空List
            propKeyPairList.clear();
            custPropKeyPairList.clear();
            propWithCustPropKeyPairList.clear();
        }
        taoBaoFullItemInfo.setTaoBaoSkuExtends(taoBaoSkuExtendList);

        // 找出重复定义sku属性；生成商品属性字符串
        if (!CollectionUtils.isEmpty(propsNameCache)) {
            Set<KeyNamePair<String, String>> repeatedPropSet = new HashSet<KeyNamePair<String, String>>();
            Map<String, List<String>> skuKeyGroupMap = new HashMap<String, List<String>>();
            for (KeyNamePair<String, String> aKeyNamePair : propsNameCache.values()) {
                List<String> skuPropNameList = skuKeyGroupMap.get(aKeyNamePair.getKey());
                if (skuPropNameList == null) {
                    skuPropNameList = new ArrayList<String>();
                    skuKeyGroupMap.put(aKeyNamePair.getKey(), skuPropNameList);
                }

                // 若同一个sku属性下，存在相同的sku属性值，则说明sku属性值重复
                if (skuPropNameList.contains(aKeyNamePair.getName())) {
                    repeatedPropSet.add(aKeyNamePair);
                }

                skuPropNameList.add(aKeyNamePair.getName());
            }
            taoBaoFullItemInfo.setRepeatedSkuProp(SkuUtil.joinRepeatedPropsStr(repeatedPropSet));
            taoBaoFullItemInfo.setProductSkuDefinition(SkuUtil
                    .joinSkuKeyGroupMap2Str(skuKeyGroupMap));
        }

        return taoBaoFullItemInfo;
    }
}
