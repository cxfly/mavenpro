package com.alibaba.lp.top.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.alibaba.lp.top.TaoBaoFullItemInfo;
import com.alibaba.lp.top.api.TopService;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.UserGetRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.api.response.UserGetResponse;


@SuppressWarnings("unused")
public class TopServiceTest {

     private static final String serviceUrl = "http://api.daily.taobao.net/router/rest";
     private static final String appKey = "402238";
     private static final String appSecret = "65aaee52cf71be8457c8bf6cd1c4da0b";

//    private static final String serviceUrl            = "http://gw.api.taobao.com/router/rest";
//    private static final String appKey                = "12349228";
//    private static final String appSecret             = "19f329ca748caa251c7524791dc92c7c";

    private TaobaoClient        taobaoTopClient;

    private static final String sessionKey_alictest01 = "61025144936e0987be561d1950734a31ee2422edf5e99432011796777";
    private static final String sessionKey_alictest02 = "6102b0539069710fe7c94bc2e1ced310c5a04687283783c2011613874";


    private static final String sessionKey            = "6102510dafb7afc8841099cb7b0972147107f3875eb6a07908372666";

    @Before
    public void init() {
        taobaoTopClient = new DefaultTaobaoClient(serviceUrl, appKey, appSecret);
    }

    @Test
    public void testItemGet() throws Exception {
        ItemGetRequest req = new ItemGetRequest();
        req.setNumIid(1500008261642L);
         req.setFields("detail_url,is_timing,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
//        req.setFields("sku,cid,property_alias,nick,title,price,modified,props,props_name,num_iid,detail_url,num,location");
        ItemGetResponse res = taobaoTopClient.execute(req, sessionKey_alictest02);
        String body = res.getBody();
        System.out.println(body + ", isSuccess= " + res.isSuccess());
    }

    @Test
    public void testItemDelisting() throws Exception {
        ItemUpdateDelistingRequest req = new ItemUpdateDelistingRequest();
        req.setNumIid(1500008261642L);
        ItemUpdateDelistingResponse res = taobaoTopClient.execute(req, sessionKey_alictest02);
        String body = res.getBody();
        System.out.println(body + ", msg==" + res.getMsg() + ", isSuccess= " + res.isSuccess());
    }

    @Test
    public void testListing() throws Exception {
        ItemUpdateListingRequest req = new ItemUpdateListingRequest();
        req.setNumIid(1500008261642L);
        req.setNum(86L);
        ItemUpdateListingResponse res = taobaoTopClient.execute(req, sessionKey_alictest02);
        System.out.println(res);
        String body = res.getBody();
        System.out.println(body + ", msg==" + res.getMsg() + ", isSuccess= " + res.isSuccess());
    }

    @Test
    public void testUserGet() throws Exception {
        UserGetRequest req = new UserGetRequest();
        req.setFields("user_id,uid,nick,sex,buyer_credit,seller_credit,location,created,last_visit,birthday,type,status,alipay_no,alipay_account,alipay_account,email,consumer_protection,alipay_bind");
        req.setNick("alictest01");
        UserGetResponse res = taobaoTopClient.execute(req, sessionKey_alictest01);
        System.out.println(res);
        String body = res.getBody();
        System.out.println(body + ", msg==" + res.getMsg() + ", isSuccess= " + res.isSuccess());
    }

    @Test
    public void testGetItemInfo() {
        TopService topService = new TopService();
        TaoBaoFullItemInfo skuList = topService.getSkuList(20495064212L);
        Assert.notNull(skuList);
    }
}
