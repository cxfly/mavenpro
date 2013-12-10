package com.cxfly.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 安全工具类，包含授权签名所需的hmac_sha1算法，主要用于计算签名
 */
public final class SecurityUtil {

    public static void main(String[] args) throws Exception {
        //        String url = "http://gw.lp.alibaba.com/openapi/xml2/1/lp.alibaba.logistics.sandbox/orderCallback/500015";
        //        String url = "http://gw.lp.alibaba.com/openapi/xml2/1/lp.alibaba.logistics.sandbox/orderCallback/500015";
        String url = "http://gw.lp.alibaba.com/openapi/xml2/2/lp.alibaba.logistics.sandbox/warehouseEntryInCallback/510030";
        String appSecret = "shttwmslp4pl";

        //        String data = "<ResultInfo><wareHouseCode>LPWH2011092624002</wareHouseCode><orderCode>148861</orderCode><status>CONSIGN</status><logisticProviderId>SF</logisticProviderId><mailNo>571974319407</mailNo><remark>jiedanshibai</remark><operator>zhuge</operator><operatorDate>2013-01-16 12:13:14</operatorDate></ResultInfo>";
        String data = "<RequestOrder><items><item><channelName>RT</channelName><itemCount>1</itemCount><itemName>【1111购物狂欢节】kaiboer/开博尔K610i双核高清硬盘播放器无线网络电视机顶盒子</itemName><itemType>normal</itemType><itemValue>879.0</itemValue><payTime>2013-11-11 00:02:57</payTime><sellerNick>开博尔影傲专卖店</sellerNick><skuCode>WL100329371</skuCode><skuName>颜色分类:延保一年送AV线</skuName><spuCode>100029496</spuCode><supplierCode>100034814</supplierCode><tradeId>453291348716920</tradeId></item></items><orderCode>162390360</orderCode><orderType>NORMAL</orderType><receiver><address>福寿东街与北海路交叉路口金诺大厦五楼 永诚保险理赔部</address><city>潍坊市</city><district>奎文区</district><mobile>15653600678</mobile><name>田鹏</name><postCode>261061</postCode><prov>山东省</prov></receiver><wareHouseCode>LPWH2013101293003</wareHouseCode></RequestOrder>";

        //解析url用于加密
        String requestLink = url.substring(url.indexOf("xml2"));
        System.out.println(requestLink);
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("_data_", data);
        String srcData = "xml2/2/lp.alibaba.logistics.sandbox/warehouseEntryInCallback/500029"
                + "_data_" + data;
        //        String srcData = "xml2/1/lp.alibaba.logistics.sandbox/orderCallback/500015" + "_data_" + data;
        String signature = SecurityUtil.hmacSha1ToHexStr(srcData, appSecret);

        System.out.println(signature);
        //httpClient post请求方式
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        method.setParameter("_data_", data);
        method.setParameter("_aop_signature", signature);

        client.executeMethod(method);
        //得到请求返回值
        String response = method.getResponseBodyAsString();
        System.out.println(response);
        method.releaseConnection();
    }

    //===============================================================================
    // 安全工具
    //===============================================================================
    public static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * HMAC see：http://www.ietf.org/rfc/rfc2104.txt
     */
    public static byte[] hmacSha1(byte[] data, byte[] key, int offset, int len) {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        mac.update(data, offset, len);
        return mac.doFinal();
    }

    public static byte[] hmacSha1(byte[][] datas, byte[] key) {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        for (byte[] data : datas) {
            mac.update(data);
        }
        return mac.doFinal();
    }

    public static byte[] hmacSha1(String[] datas, byte[] key) {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        try {
            for (String data : datas) {
                mac.update(data.getBytes(StringUtil.CHARSET_NAME_UTF8));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return mac.doFinal();
    }

    public static String hmacSha1ToHexStr(byte[] data, byte[] key, int offset, int len) {
        byte[] rawHmac = hmacSha1(data, key, offset, len);
        return StringUtil.encodeHexStr(rawHmac);
    }

    public static String hmacSha1ToHexStr(byte[] data, String key, int offset, int len) {
        try {
            return hmacSha1ToHexStr(data, key.getBytes(StringUtil.CHARSET_NAME_UTF8), offset, len);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String hmacSha1ToHexStr(String str, String key) {
        try {
            byte[] data = str.getBytes(StringUtil.CHARSET_NAME_UTF8);
            return hmacSha1ToHexStr(data, key.getBytes(StringUtil.CHARSET_NAME_UTF8), 0,
                    data.length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private SecurityUtil() {
    }

    static final class StringUtil {

        public static final String CHARSET_NAME_UTF8        = "UTF-8";
        public static final char[] digital                  = "0123456789ABCDEF".toCharArray();
        public static final String DEFAULT_DATA_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

        public static String format(Date date) {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATA_TIME_FORMAT);
            return format.format(date);
        }

        public static String encodeHexStr(final byte[] bytes) {
            if (bytes == null) {
                return null;
            }
            char[] result = new char[bytes.length * 2];
            for (int i = 0; i < bytes.length; i++) {
                result[i * 2] = digital[(bytes[i] & 0xf0) >> 4];
                result[i * 2 + 1] = digital[bytes[i] & 0x0f];
            }
            return new String(result);
        }

        public static byte[] decodeHexStr(final String str) {
            if (str == null) {
                return null;
            }
            char[] charArray = str.toCharArray();
            if (charArray.length % 2 != 0) {
                throw new RuntimeException("hex str length must can mod 2, str:" + str);
            }
            byte[] bytes = new byte[charArray.length / 2];
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                int b;
                if (c >= '0' && c <= '9') {
                    b = (c - '0') << 4;
                } else if (c >= 'A' && c <= 'F') {
                    b = (c - 'A' + 10) << 4;
                } else {
                    throw new RuntimeException("unsport hex str:" + str);
                }
                c = charArray[++i];
                if (c >= '0' && c <= '9') {
                    b |= c - '0';
                } else if (c >= 'A' && c <= 'F') {
                    b |= c - 'A' + 10;
                } else {
                    throw new RuntimeException("unsport hex str:" + str);
                }
                bytes[i / 2] = (byte) b;
            }
            return bytes;
        }

        public static String toString(final byte[] bytes) {
            if (bytes == null) {
                return null;
            }
            try {
                return new String(bytes, CHARSET_NAME_UTF8);
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        public static String toString(final byte[] bytes, String charset) {
            if (bytes == null) {
                return null;
            }
            try {
                return new String(bytes, charset);
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        public static byte[] toBytes(final String str) {
            if (str == null) {
                return null;
            }
            try {
                return str.getBytes(CHARSET_NAME_UTF8);
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        private StringUtil() {
        }
    }
}
