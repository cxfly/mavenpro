/*
 * Copyright (c) 2008 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cxfly.test.amino;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.amino.ds.lockfree.LockFreeDictionary;

/*   The Dictionary is a map contains elements with key and value pairs.
 *   This example show the main interfaces of LockFreePriorityDictionary.
 *   Including : put(), deleteValue(), size(), isEmpty()
 *   
 *   The DictAdder class will add items of String from "1" to "ELEMENT_NUM".
 *   The DictRemover class will remove no more than ELEMENT_NUM items.
 *   The main thread will wait for all the threads in pool to finish.  
 *   2009/05/22
 */
public class DictionaryExample {
    public static final int  ELEMENT_NUM = 100;
    private static final int TASKS_NUM   = 4;

    public static void main(String[] argvs) {

        ExecutorService exec = Executors.newFixedThreadPool(TASKS_NUM);

        final Map<String, String> dictionaryStr = new LockFreeDictionary<String, String>();

        for (int i = 0; i < TASKS_NUM; ++i) {
            exec.submit(new DictAdder(dictionaryStr));
            if (i % 2 == 0)
                exec.submit(new DictRemover(dictionaryStr));
        }

        exec.shutdown();

        // Wait until all the threads in pool to finish.
        try {
            while (!exec.awaitTermination(600, TimeUnit.SECONDS))
                ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // If the Dictionary is empty now, the program will exit.
        if (dictionaryStr.isEmpty()) {
            System.out.println("The Dictionary is empty, so I will exit!");
            System.exit(1);
        }

        System.out.println("Dictionary size: " + dictionaryStr.size());
    }
}

/*
 * The DictAdder class will add elements to the given Dictionary.
 */
class DictAdder implements Runnable {
    Map<String, String> taskDict;
    private int         count = 0;

    public DictAdder(Map<String, String> taskMap) {
        taskDict = taskMap;
    }

    @Override
    public void run() {
        while (count < DictionaryExample.ELEMENT_NUM) {
            String putStr = taskDict.put(Integer.toString(count++), Integer.toString(count++));
            System.out.println(Thread.currentThread() + " Put:" + putStr);
        }
        Thread.yield();
    }
}

/*
 * The DictRemover class will remove the existed elements from the given
 * Dictionary.
 */
class DictRemover implements Runnable {
    Map<String, String> taskDict;
    private int         count = 0;

    public DictRemover(Map<String, String> taskMap) {
        taskDict = taskMap;
    }

    @Override
    public void run() {
        int times = 0;

        while (times < DictionaryExample.ELEMENT_NUM) {
            String key = ((LockFreeDictionary<String, String>) taskDict).deleteValue(Integer
                    .toString(count++));
            System.out.println(Thread.currentThread() + " Removed key:" + key);
            times++;
        }
        Thread.yield();
    }
}
