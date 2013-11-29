package com.alibaba.lp.express;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.lp.express.excel.DefaultCellStyleServiceImpl;
import com.alibaba.lp.express.excel.ExcelUtil;
import com.alibaba.lp.express.excel.Page;
import com.alibaba.lp.express.excel.PageQueryCallback;

/**
 * 获得物流跟踪信息工具类
 * 
 * @author john
 */
public class LogisticsTraceUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsTraceUtil.class);

    private static String       key    = "bit6723gHEf";                                    // MD5加密key

    private static int          type   = 5;                                                // B2B公司

    private static LogisticsTraceResult getTraceResult(String companyId, String mailNo, int type, String key)
                                                                                                             throws Exception {
        String url = "http://partner.taobao.com/call/out_json_trace_query.do?company_id=" + companyId + "&mail_no="
                     + mailNo + "&type=" + type + "&digest="
                     + URLEncoder.encode(calculateDigest(companyId, mailNo, key, type), "utf-8");
        if (logger.isDebugEnabled()) {
            logger.debug("[logistics trace][url] " + url);
        }

        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
        client.executeMethod(getMethod);
        // 获得结果
        String json = getMethod.getResponseBodyAsString();

        if (logger.isDebugEnabled()) {
            logger.debug("[logistics trace][json] " + json);
        }
        ObjectMapper jsonParser = new ObjectMapper();
        jsonParser.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); // 淘宝接口混合使用单引号和双引号。
        jsonParser.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置JSON日期解析参数。淘宝接口可能在同一响应中返回多种日期格式。
        MultipleDateFormat format = new MultipleDateFormat();
        format.addDateFormats(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                              new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        format.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        // jsonParser.getDeserializationConfig().setDateFormat(format);
        jsonParser.setDateFormat(format);
        LogisticsTraceResult readValue = null;
        try {
            readValue = jsonParser.readValue(json, LogisticsTraceResult.class);
        } catch (Exception e) {
            System.out.println("json: " + json);
            e.printStackTrace();
        }

        return readValue;
    }

    private static String calculateDigest(String companyId, String mailNo, String key, int type) {
        String result = StringUtils.EMPTY;
        String stringToSign = companyId + mailNo + key;
        try {
            return new String(Base64.encodeBase64(DigestUtils.md5(stringToSign.getBytes("utf8")), false, false));
        } catch (UnsupportedEncodingException e) {
            if (logger.isInfoEnabled()) {
                logger.info("[Base64 Error]" + e.getMessage());
            }
        }
        return result;
    }

    /*
     * public static void main(String[] args) throws Exception { String str = "9994440121";
     * System.out.println(Integer.parseInt(str)); String companyId = "500"; String mailNo = "205018894233"; String url =
     * "http://partner.taobao.com/call/out_json_trace_query.do?company_id=" + companyId + "&mail_no=" + mailNo +
     * "&type=" + type + "&digest=" + URLEncoder.encode(calculateDigest(companyId, mailNo, key, type), "utf-8");
     * System.out.println(url); }
     */

    public static void main(String[] args) {
        LogisticsTraceUtil t = new LogisticsTraceUtil();
        try {
            t.process();
//             t.simepleTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simepleTest() {
        String companyId = "100";
        String mailNo = "888146713967";
        try {
            LogisticsTraceResult taobaoTraceResult = getTraceResult(companyId, mailNo, type, key);
            if (taobaoTraceResult == null) {
                System.out.println("null");
            } else  if (!StringUtils.isEmpty(taobaoTraceResult.getErrorMsg()) || taobaoTraceResult.getTraces() == null
                || taobaoTraceResult.getTraces().size() <= 0) {
                System.out.println("[ERROR]" + taobaoTraceResult.getTraces());
                System.out.println("[ERROR]" + taobaoTraceResult.getErrorMsg());
            } else {
                List<LogisticsTraceStep> taobaoTraceSteps = taobaoTraceResult.getTraces();
                for (LogisticsTraceStep taobaoTraceStep : taobaoTraceSteps) {
                    System.out.println("time --> " + taobaoTraceStep.getAcceptTime());
                    System.out.println("add --> " + taobaoTraceStep.getAcceptAddress());
                    System.out.println("remark --> " + taobaoTraceStep.getRemark());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private void process() throws Exception {
        List dataList = this.parseCsv();
        this.expressNo(dataList);
    }

    private List<TraceResult> parseCsv() throws Exception {
        File dir = new File("E:/Download/tmp");
        File[] files = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith("csv")) {
                    return true;
                }
                return false;
            }
        });

        List<TraceResult> dataList = new ArrayList<TraceResult>();
        for (File file : files) {
            Thread.sleep(10);
            List<String> readLines = FileUtils.readLines(file, "GBK");
            System.out.println("file: " + file.getName() + ", lineCount: " + readLines.size());
            if (!CollectionUtils.isEmpty(readLines)) {
                for (String aLine : readLines) {
                    // System.out.println(aLine);
                    aLine = aLine.replace("\"", "");
                    // System.out.println(aLine);
                    String[] splits = StringUtils.split(aLine, ",");
                    if (splits.length != 5) {
                        throw new RuntimeException();
                    }

                    if ("CHANNEL_ORDER_NO".equals(splits[0])) {
                        continue;
                    }

                    TraceResult bean = new TraceResult();
                    bean.setTid(splits[0]);
                    bean.setGmtPay(splits[1]);
                    bean.setExpressName(splits[2]);
                    bean.setExpressCode(splits[3]);
                    bean.setExpressNo(splits[4]);

                    String companyId = ReadWlCompanyPropertiesUtil.siteMapProperties(splits[3]);
                    String mailNo = StringUtils.trim(splits[4]);
                    LogisticsTraceResult taobaoTraceResult = getTraceResult(companyId, mailNo, type, key);
                    
                    if (taobaoTraceResult == null) {
                        System.out.println("[ERROR]: taobaoTraceResult is null");
                    } else if (!StringUtils.isEmpty(taobaoTraceResult.getErrorMsg()) || taobaoTraceResult.getTraces() == null
                        || taobaoTraceResult.getTraces().size() <= 0) {
                        System.out.println("[ERROR]" + taobaoTraceResult.getErrorMsg() + "expressCode: " + splits[3]
                                           + ", companyId: " + companyId + ", mailNo:" + mailNo + ", tid:" + splits[0]);
                    } else {
                        List<LogisticsTraceStep> taobaoTraceSteps = taobaoTraceResult.getTraces();
                        Collections.sort(taobaoTraceSteps);
                        bean.setFirstTraceTime(DateUtils.format(taobaoTraceSteps.get(0).getAcceptTime()));
                        StringBuilder buf = new StringBuilder();
                        int row = 0;
                        for (LogisticsTraceStep step : taobaoTraceSteps) {
                            if (row>0) {
                                buf.append("\n");
                            }
                            buf.append("时间: ").append(DateUtils.format(step.getAcceptTime()));
                            buf.append(",地址: ").append(step.getAcceptAddress());
                            buf.append(",备注: ").append(step.getRemark());
                            row++;
                        }
                        bean.setTraceContent(buf.toString());
                    }
                    dataList.add(bean);
                }
            }
        }

        return dataList;
    }



    private void expressNo(final List<Object> dataList) throws Exception {
        OutputStream os = new FileOutputStream("E:/Download/tmp/expressNo2.xlsx");
        ExcelUtil.largeDataExport(os, dataDefine, new DefaultCellStyleServiceImpl(), new PageQueryCallback() {

            @Override
            public Page<?> getPage(int pageNo) {
                Page<Object> page = new Page<Object>();
                if (pageNo == 1) {
                    page.setDatas(dataList);
                }
                return page;
            }
        });
        os.close();
        System.out.println("generate excel sucess");
    }

    private final static String[][] dataDefine = { { "订单号", "tid", "120" }, { "支付时间", "gmtPay", "150" },
            { "首条流转信息时间", "firstTraceTime", "150" }, { "快递公司名称", "expressName", "100" },
            { "快递公司编码", "expressCode", "60" }, { "运单号", "expressNo", "100" }, { "流转信息详情", "traceContent", "800" } };

}
