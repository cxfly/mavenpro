package com.cxfly.lp.express;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String format(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null) {
            return simpleFormat.format(date);
        }
        return null;
    }
}
