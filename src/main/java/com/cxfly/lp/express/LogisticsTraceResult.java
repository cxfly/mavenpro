package com.cxfly.lp.express;

import java.io.Serializable;
import java.util.List;

public class LogisticsTraceResult implements Serializable {

    /**
     * 
     */
    private static final long        serialVersionUID = 7423742163998891679L;
    /**
     * 单步跟踪信息
     */
    private List<LogisticsTraceStep> traces;
    /**
     * 错误说明字符串
     */
    private String                   errorMsg;

    public void setTraces(List<LogisticsTraceStep> traces) {
        this.traces = traces;
    }

    public List<LogisticsTraceStep> getTraces() {
        return traces;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
