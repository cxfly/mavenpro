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

/*   The list contains the given types of items. 
 *   This example shows the mainly interfaces of LockFreeList.
 *   Including : add(), remove(), isEmpty(), size(), iterator().
 *   
 *   The ListAdder class will add different items of String from "1" to "ELEMENT_NUM".
 *   The ListRemover class will remove no more than ELEMENT_NUM items.
 *   The main thread will wait for all the threads in pool to finish.  
 *   2009/05/22
 */

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.amino.ds.lockfree.LockFreeList;

public class ListExample {

    public static final int  ELEMENT_NUM = 100;
    private static final int TASKS_NUM   = 4;

    public static void main(String[] argvs) {

        ExecutorService exec = Executors.newFixedThreadPool(TASKS_NUM);

        final List<String> listStr = new LockFreeList<String>();

        for (int i = 0; i < TASKS_NUM; ++i) {
            exec.submit(new ListAdder(listStr));
            exec.submit(new ListRemover(listStr));
        }

        exec.shutdown();

        // Wait until all the threads in pool to finish.
        try {
            while (!exec.awaitTermination(600, TimeUnit.SECONDS))
                ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // If the List is empty now, the program will exit.
        if (listStr.isEmpty()) {
            System.out.println("The List is empty, so I will exit!");
            System.exit(1);
        }

        // Print all the elements in List.
        for (String str : listStr)
            System.out.println(str);

        System.out.println("List size before clearing: " + listStr.size());
        listStr.clear();
        System.out.println("List size after clearing: " + listStr.size());
    }
}

/*
 * The ListAdder class will add elements to the given List.
 */
class ListAdder implements Runnable {
    List<String>                 taskLst;
    private static AtomicInteger count = new AtomicInteger();

    public ListAdder(List<String> taskList) {
        taskLst = taskList;
    }

    @Override
    public void run() {
        while (taskLst.size() < ListExample.ELEMENT_NUM) {
            if (taskLst.add(Integer.toString(count.incrementAndGet()))) {
                System.out.println(Thread.currentThread() + " Added " + count.get());
            }
        }
        Thread.yield();
    }
}

/*
 * The ListRemover class will remove the existed elements from the given List.
 */
class ListRemover implements Runnable {
    List<String>                 taskLst;
    private static AtomicInteger count = new AtomicInteger();

    public ListRemover(List<String> taskList) {
        taskLst = taskList;
    }

    @Override
    public void run() {
        int times = 0;

        while (times < ListExample.ELEMENT_NUM) {
            if (taskLst.remove(Integer.toString(count.incrementAndGet()))) {
                System.out.println(Thread.currentThread() + "Removed: " + count.get());
            }
            times++;
        }
        Thread.yield();
    }
}
