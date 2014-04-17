package com.cxfly.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Test3 {
    public static void main(String[] args) throws Exception {
        Output output = new Output(4096);
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.register(Simple.class);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        output.setOutputStream(new FileOutputStream("r:/xxx.txt"));
        kryo.writeClassAndObject(output, Simple.getSimple());
        output.flush();
        output.close();
        
        
        Input input = new Input(4096);
        input.setInputStream(new FileInputStream("r:/xxx.txt"));
        Object obj = kryo.readObject(input, Simple.class);
        input.close();
        System.out.println(obj);
    }
    
    
    
    
    
    public static class Simple implements java.io.Serializable{
        private static final long serialVersionUID = 320217802995289230L;
        private String name;
        private int age;


        private Simple() {
            super();
        }

        public String getName() {
          return name;
        }

        public void setName(String name) {
          this.name = name;
        }

        public int getAge() {
          return age;
        }

        public void setAge(int age) {
          this.age = age;
        }

        @Override
        public String toString() {
            return "Simple [name=" + name + ", age=" + age + "]";
        }

        public static Simple getSimple() {
          Simple simple = new Simple();
          simple.setAge(10);
          simple.setName("小米");
          return simple;
        }
      }
}
