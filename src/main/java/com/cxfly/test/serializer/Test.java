package com.cxfly.test.serializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Test {
    public static void main(String[] args) throws Exception {
        jibxTest();
        //        xmlTest();
        //        jsonTest();
    }

    public static void xmlTest() {
        //        XStream xstream = new XStream();
        //        XStream xstream = new XStream(new DomDriver());
        XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
        xstream.autodetectAnnotations(true);
        //        xstream.alias("trade", Trade.class);
        //        xstream.alias("address", Address.class);
        //        xstream.alias("order", Order.class);
        Trade trade = getTrade();
        String xml = xstream.toXML(trade);
        System.out.println(xml);

        Object fromXML = xstream.fromXML(xml);
        System.out.println(fromXML);
    }

    public static void jsonTest() {
        Trade trade = getTrade();
        String jsonString = JSON.toJSONString(trade);
        System.out.println(jsonString);

        Trade parseObject = JSON.parseObject(jsonString, Trade.class);
        System.out.println(parseObject);
    }

    /**
     * E:\Study\WebHttpUtils>java -cp
     * bin;lib/jibx-tools.jar;lib/log4j-1.2.16.jar
     * org.jibx.binding.BindingGenerator -f bind.xml com.hoo.entity.Account
     * 
     * <pre>
     * java -cp bin;lib/jibx-tools.jar;lib/log4j-1.2.16.jar org.jibx.binding.generator.BindGen -b  bind.xml com.cxfly.test.serializer.Trade com.cxfly.test.serializer.Order com.cxfly.test.serializer.Address
     * java -cp bin;lib/jibx-bind.jar org.jibx.binding.Compile -v bind.xml
     * </pre>
     * 
     * @throws Exception
     */
    public static void jibxTest() throws Exception {
        IBindingFactory bfact = BindingDirectory.getFactory(Trade.class);
        Trade trade = getTrade();
        IMarshallingContext mctx = bfact.createMarshallingContext();

        StringWriter sw = new StringWriter();
        mctx.setIndent(2);
        mctx.marshalDocument(trade, "UTF-8", null, sw);
        String xmlString = sw.toString();
        System.out.println(xmlString);

        IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
        Object parseObject = uctx.unmarshalDocument(IOUtils.toInputStream(xmlString, "utf-8"),
                "utf-8");
        System.out.println(parseObject);
    }

    private static Trade getTrade() {
        Trade trade = new Trade();
        trade.setItemList(new String[] { "22222222", "33333" });
        trade.setTradeNo("111111111");
        trade.setAddress(new Address("杭州市j"));
        List<Order> orderList = new ArrayList<Order>();
        orderList.add(new Order("abc", 3));
        orderList.add(new Order("ddd", 6));
        trade.setOrderList(orderList);
        return trade;
    }

}
