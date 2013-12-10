package com.cxfly.test;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.cxfly.test.SecurityUtil.StringUtil;

public class Test {
    static String url = "http://10.68.146.2:8080/TTD1/TPperfServlet";

    public static void main1(String[] args) {
        final AtomicLong count = new AtomicLong();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread("T_" + i) {
                @Override
                public synchronized void run() {
                    for (int i = 0; i < 10000; i++) {
                        try {
                            send();
                            System.out.println(count.getAndIncrement());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
            };
            t.start();
        }

    }

    @SuppressWarnings("unused")
    private static void send() throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        client.executeMethod(method);
        //得到请求返回值
        String response = method.getResponseBodyAsString();
        //        System.out.println(response);
        method.releaseConnection();
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String data = "<RequestOrder><items><item><channelName>RT</channelName><itemCount>1</itemCount><itemName>【1111购物狂欢节】kaiboer/开博尔K610i双核高清硬盘播放器无线网络电视机顶盒子</itemName><itemType>normal</itemType><itemValue>879.0</itemValue><payTime>2013-11-11 00:02:57</payTime><sellerNick>开博尔影傲专卖店</sellerNick><skuCode>WL100329371</skuCode><skuName>颜色分类:延保一年送AV线</skuName><spuCode>100029496</spuCode><supplierCode>100034814</supplierCode><tradeId>453291348716920</tradeId></item></items><orderCode>162390360</orderCode><orderType>NORMAL</orderType><receiver><address>福寿东街与北海路交叉路口金诺大厦五楼 永诚保险理赔部</address><city>潍坊市</city><district>奎文区</district><mobile>15653600678</mobile><name>田鹏</name><postCode>261061</postCode><prov>山东省</prov></receiver><wareHouseCode>LPWH2013101293003</wareHouseCode></RequestOrder>";
        String appSecret = "ttkdwms4pl";
        String signature = SecurityUtil.hmacSha1ToHexStr("510030" + data, appSecret);
        String str1 = "118F7FC58D8B6B37209CBF858D039359D6CEE5C4";

        System.out.println(signature);

        String[] datas = new String[2];
        datas[0] = Integer.toString(510030);
        datas[1] = data;

        String signature1 = SecurityUtil.StringUtil.encodeHexStr(SecurityUtil.hmacSha1(datas,
                StringUtil.toBytes(appSecret)));

        System.out.println(signature1);
    }
}
