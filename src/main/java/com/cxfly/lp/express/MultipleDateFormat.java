package com.cxfly.lp.express;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MultipleDateFormat extends DateFormat {

    private static final long      serialVersionUID = -6864774472000389484L;

    private final List<DateFormat> formats          = new ArrayList<DateFormat>();

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        throw new UnsupportedOperationException(
                "MultipleDateFormat is only for date parsing purpose");
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        for (DateFormat format : formats) {
            Date date = format.parse(source, pos);
            if (date != null)
                return date;
        }
        return null;
    }

    @Override
    public TimeZone getTimeZone() {
        throw new UnsupportedOperationException("Multiple time zone");
    }

    /**
     * 为所有的内含日期格式设置统一的时区
     */
    @Override
    public void setTimeZone(TimeZone zone) {
        for (DateFormat format : formats) {
            format.setTimeZone(zone);
        }
    }

    /**
     * 添加支持的解析格式
     * 
     * @param formatsToAdd
     */
    public void addDateFormats(List<DateFormat> formatsToAdd) {
        formats.addAll(formatsToAdd);
    }

    /**
     * 添加支持的解析格式
     * 
     * @param formatsToAdd
     */
    public void addDateFormats(DateFormat... formatsToAdd) {
        addDateFormats(Arrays.asList(formatsToAdd));
    }

    @Override
    public Object clone() {
        MultipleDateFormat multipleFormat = new MultipleDateFormat();
        multipleFormat.addDateFormats(formats);
        return multipleFormat;
    }
}
