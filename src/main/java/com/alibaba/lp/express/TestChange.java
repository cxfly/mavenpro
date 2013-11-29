package com.alibaba.lp.express;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class TestChange {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("LPWH201109062002", 100000100);
        map.put("LPWH201109226007", 100000111);
        map.put("LPWH201109225008", 100000120);
        map.put("LPWH201109225009", 100000122);
        map.put("LPWH201109226014", 100000123);
        map.put("LPWH201109297002", 100000124);
        map.put("LPWH2012050315002", 100000900);
        map.put("LPWH2012050816002", 100001000);
        map.put("LPWH2012052420002", 100001201);
        map.put("LPWH2012053022002", 100001300);
        map.put("LPWH2012061123002", 100001400);
        map.put("LPWH2012071131002", 100001701);
        map.put("LPWH2012071632002", 100001702);
        map.put("LPWH2012080635002", 100001801);
        map.put("LPWH2012080736002", 100001802);
        map.put("LPWH2012080937002", 100001803);
        map.put("LPWH2012082940002", 100001809);
        map.put("LPWH2012101245002", 100002005);
        map.put("LPWH2012101747002", 100002007);
        map.put("LPWH2012101747003", 100002008);
        map.put("LPWH2012101747005", 100002011);
        map.put("LPWH2012101748007", 100002020);
        map.put("LPWH2012102449004", 100002026);
        map.put("LPWH2012102952003", 100002031);
        map.put("LPWH2012110255002", 100002034);
        map.put("LPWH2012110556002", 100002035);
        map.put("LPWH2012110556003", 100002036);
        map.put("LPWH2012110757005", 100002042);
        map.put("LPWH2012120759002", 100002104);
        map.put("LPWH2012120759003", 100002105);
        map.put("LPWH2012120760003", 100002107);

        List<String> readLines = FileUtils.readLines(new File(""));
        for (String line : readLines) {
            System.out.println(line);
        }

    }

}
