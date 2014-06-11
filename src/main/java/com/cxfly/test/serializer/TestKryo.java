package com.cxfly.test.serializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.cxfly.test.Order;
import com.cxfly.test.Trade;
import com.cxfly.test.TradeStatus;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

@SuppressWarnings("unused")
public class TestKryo {
	private static final String HESSIAN_FILE = "r:/hessian.bin";
	private static final String KRYO_FILE = "r:/kryo.bin";
	private static final String JSON_FILE = "R:/json.txt";

	public static void main(String[] args) throws Exception {
		int times = 1000;
		
		Object obj = generateData();
		long s1 = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			testFastJson(obj);
		}
		long s2 = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			testHessian(obj);
		}
		long s3 = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			testKryo(obj);
		}
		long s4 = System.currentTimeMillis();

		System.out.println("json : " + (s2 - s1));
		System.out.println("hessian : " + (s3 - s2));
		System.out.println("kryo : " + (s4 - s3));
	}

	private static Trade[] generateData() {
		Trade[] obj = new Trade[1];
		for (int i = 0; i < obj.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("zhang" + i, i + "测试");
			map.put("zhang" + i, i + " 测试tt测");
			Trade td = new Trade("zhangSan", 100, map, i);
			td.setGmtCreate(new Date());
			td.setTradeStatus(TradeStatus.SENT);
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(new Order(432L, "OD23424"));
			orderList.add(new Order(111L, "OD3333333"));
			td.setOrderList(orderList);
			obj[i] = td;
		}
		return obj;
	}

	static void testFastJson(Object obj) throws Exception {

		String jsonString = JSON.toJSONString(obj);
		// String jsonString = JSON.toJSONString(obj,
		// SerializerFeature.WriteClassName);
		FileWriter writer = new FileWriter(JSON_FILE);
		writer.write(jsonString);
		writer.close();
		// Object parseObject = JSON.parseArray(jsonString);
		Object parseObject = JSON.parseArray(jsonString);
	}

	static void testHessian(Object v) throws Exception {
		Hessian2Output out = new Hessian2Output(new FileOutputStream(HESSIAN_FILE));
		out.writeObject(v);
		out.close();
		Hessian2Input in = new Hessian2Input(new java.io.FileInputStream(HESSIAN_FILE));
		Object deserialize = in.readObject();
		// System.out.println(deserialize);
	}

	private static Kryo getKryo() {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(true);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		
		kryo.register(com.cxfly.test.Trade[].class);
		kryo.register(com.cxfly.test.Trade.class);
		kryo.register(com.cxfly.test.TradeStatus.class);
		kryo.register(java.util.Date.class);
		kryo.register(java.util.HashMap.class);
		kryo.register(java.util.ArrayList.class);
		kryo.register(com.cxfly.test.Order.class);
		
		return kryo;
	}

	public static void testKryo(Object data) throws Exception {
		Output output = new Output(new FileOutputStream(KRYO_FILE));
		Kryo kryo = getKryo();
		kryo.writeObject(output, data);
		// kryo.writeClassAndObject(output, data);
		output.flush();
		output.close();

		kryo = getKryo();
		Input input = new Input(new FileInputStream(KRYO_FILE));
		Object obj = kryo.readObject(input, data.getClass());
		input.close();
		// System.out.println(obj);
	}
}
