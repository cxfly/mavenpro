package com.cxfly.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class TestSerial {
    public static void main(String[] args) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("zhang0", 2343 + "测试");
        map.put("zhang1", 333 + " 测试tt测");
        Trade obj = new Trade("zhang" + 555, (444 + 1), map, 24323L);
        obj.setGmtCreate(new Date());
        obj.setTradeStatus(TradeStatus.SENT);
        List<Order> orderList =new ArrayList<Order>();
        orderList.add(new Order(432L,"OD23424"));
        orderList.add(new Order(111L,"OD3333333"));
        obj.setOrderList(orderList);

        TestSerial t = new TestSerial();
        //        t.testHessian(obj);

        t.testKryo(obj);

        testKyro();
    }

    void testHessian(Object v) {
        try {
            Hessian2Output out = new Hessian2Output(new java.io.FileOutputStream("r:/obj2.txt"));
            out.writeObject(v);
            out.close();
            Hessian2Input in = new Hessian2Input(new java.io.FileInputStream("r:/obj2.txt"));
            Object deserialize = in.readObject();
            System.out.println(deserialize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void testKryo(Object v) {
        try {

            String file = "kryo.txt";
            Output output = new Output(new FileOutputStream(file));
            Kryo kryo = this.getKryo();
            System.out.println("OLD: " + v);
            //            v = "你好";
            kryo.writeClassAndObject(output, v);
            output.flush();
            output.close();

            kryo = this.getKryo();
            Input input = new Input(new FileInputStream(file));
            Object obj = kryo.readClassAndObject(input);
            input.close();
            System.out.println(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Kryo getKryo() {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    }

    public static void setSerializableObject() throws IOException {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(Trade.class);

        Output output = new Output(new FileOutputStream("file.bin"));
        for (int i = 0; i < 1; i++) {
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("zhang0", i + "测试");
            map.put("zhang1", i + " 测试tt测");
            Trade trade = new Trade("zhang" + i, (i + 1), map, 24323L);
            trade.setGmtCreate(new Date());
            kryo.writeObject(output, trade);
        }
        output.flush();
        output.close();
    }

    public static void getSerializableObject() {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        Input input;
        try {
            input = new Input(new FileInputStream("file.bin"));
            Trade simple = null;
            while ((simple = kryo.readObject(input, Trade.class)) != null) {
                System.out.println(simple);
            }

            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KryoException e) {

        }

    }

    private static void testKyro() throws Exception {
        long s1 = System.nanoTime();
        setSerializableObject();
        long s2 = System.nanoTime();
        System.out.println("java Serializable writeObject time:" + (s2 - s1) + " ms");
        getSerializableObject();
        System.out.println("java Serializable readObject time:" + (System.nanoTime() - s2) + " ms");

    }

}
