package com.cxfly.ttyd;

public class SQLGenerator {

    static int    i;
    static String seq = "00";

    public static void main(String[] args) {
        StringBuilder buf = new StringBuilder();
        doGenerateSql(buf);

        System.out.println(buf.toString());
    }

    public static void doGenerateSql(StringBuilder buf) {
        for (int i = 0; i < 128; i++) {
            if (i > 0) {
                buf.append(" union ");
            }
            generate_sms_Sql(buf);
        }
    }

    public static void generate_sms_Sql(StringBuilder buf) {
        String tableName = "t_sms_message";
        String collumName = "tel";
        buf.append("select t.").append(collumName).append(",count(*) from ");
        buf.append("`").append(tableName).append(i++).append("`");
        buf.append(" t ");
        buf.append("group by t.").append(collumName).append(" having count(*)>1000");
    }
    
    public static void generate_duancai_Sql(StringBuilder buf) {
        String date = "2014-01-01";
        String tableName = "sms_sender";
        String collumName = "tel_number";
        buf.append("select t.").append(collumName).append(",count(*) from `");
        String seqStr = String.valueOf(i++);
        seqStr = seq.substring(0, seq.length() - seqStr.length()) + seqStr;
        buf.append(tableName).append(seqStr);
        buf.append("` t where t.`create_time`>'");
        buf.append(date);
        buf.append("' group by t.").append(collumName).append(" having count(*)>1000");
    }

}
