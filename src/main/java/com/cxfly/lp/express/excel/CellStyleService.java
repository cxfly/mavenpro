package com.cxfly.lp.express.excel;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface CellStyleService {

    /**
     * @param wb 需自定义的workbook
     * @param sheet 需自定义的sheet页
     * @param dataList 行记录
     * @param objMds 列记录
     * @param rowNumStart 从第几行开始
     */
    public int buildSheetStyle(Workbook wb, Sheet sheet, List<?> dataList, Method[] objMds,
                               int rowNumStart) throws Exception;
}
