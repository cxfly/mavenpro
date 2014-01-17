package com.cxfly.test.model.test;

import java.util.ArrayList;
import java.util.List;

import com.cxfly.test.model.OrderEntryDTO;
import com.cxfly.test.model.OrderItem;
import com.cxfly.test.model.ReceiverDTO;
import com.cxfly.test.model.util.DTOConvertUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Test {
    public static void main(String[] args) {
        OrderEntryDTO order = new OrderEntryDTO();
        order.setOrderCode("11111");
        order.setOrderType("1111");
        order.setRemarks("xxx");
        order.setWareHouseCode("L24234");

        ReceiverDTO receiver = new ReceiverDTO();
        receiver.setProv("浙江省");
        receiver.setCity("杭州市");
        receiver.setDistrict("西湖区");
        receiver.setAddress("文三路100号");
        receiver.setMobile("13914234242");
        receiver.setPhone("0571-234324234");
        receiver.setPostCode("310100");
        order.setReceiver(receiver);

        List<OrderItem> items = new ArrayList<OrderItem>();
        OrderItem e1 = new OrderItem();
        e1.setSkuCode("LP24234");
        e1.setSkuName("测试商品");
        e1.setItemCount(3);
        e1.setItemName("测试商品");
        e1.setItemValue(2423d);
        OrderItem e2 = new OrderItem();
        e2.setSkuCode("LP1111111");
        e2.setSkuName("测试商品2");
        e2.setItemCount(2);
        e2.setItemName("测试商品1111");
        e2.setItemValue(2423d);
        items.add(e1);
        items.add(e2);
        order.setItems(items);

        Object bean2dto = DTOConvertUtil.bean2dto(order);
        System.out.println(bean2dto);

        XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
        //   xstream.autodetectAnnotations(true);

        String xml = xstream.toXML(bean2dto);
        System.out.println(xml);

        Object fromXML = xstream.fromXML(xml);
        System.out.println(fromXML);
    }
}
