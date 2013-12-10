package com.cxfly.lp.express;

public class TraceResult {

    private String tid;
    private String gmtPay;
    private String expressName;
    private String expressCode;
    private String expressNo;
    private String firstTraceTime;
    private String traceContent;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getGmtPay() {
        return gmtPay;
    }

    public void setGmtPay(String gmtPay) {
        this.gmtPay = gmtPay;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getFirstTraceTime() {
        return firstTraceTime;
    }

    public void setFirstTraceTime(String firstTraceTime) {
        this.firstTraceTime = firstTraceTime;
    }

    public String getTraceContent() {
        return traceContent;
    }

    public void setTraceContent(String traceContent) {
        this.traceContent = traceContent;
    }
}
