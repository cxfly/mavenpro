package com.alibaba.lp.express.excel;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.CollectionUtils;

/**
 * Excel相关处理方法类
 * 
 * @author weirui.shenwr 2011-8-11
 */
public class ExcelUtil {

    public static final String  EXCLE_2003          = "xls";

    public static final String  EXCLE_2007          = "xlsx";

    public static final String  RESULT_SUFFIX       = "_result";

    public static final Integer DEFAULT_WINDOW_SIZE = 100;


    /**
     * 写数组数据到一个excel行
     * 
     * @param sheet
     * @param values
     * @param style
     */
    public static void writeRow(Sheet sheet, String[] values, CellStyle style, int rowNum) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
            cell.setCellValue(values[i]);
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 写一个对象到一个excle行
     * 
     * @param sheet
     * @param dataObj
     * @param properties 如getXXXX，按属性获取对象内容
     * @param style
     * @throws Exception
     */
    public static void writeRow(Sheet sheet, Object dataObj, String[] properties, CellStyle style,
                                int rowNum) throws Exception {
        Row row = sheet.createRow(rowNum);
        Class<?> cls = dataObj.getClass();
        for (int i = 0; i < properties.length; i++) {
            Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
            Method m1 = cls.getDeclaredMethod("get" + properties[i].substring(0, 1).toUpperCase()
                    + properties[i].substring(1));
            Object value = m1.invoke(dataObj);
            setCellValue(cell, value);
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 写一组对象到excel sheet
     * 
     * @param sheet
     * @param dataObj
     * @param getproperties 按属性的get方法获取对象内容
     * @param style
     * @throws Exception
     */
    public static int writeRow(Workbook wb, Sheet sheet, List<?> dataList, String[] properties,
                               int rowNumStart, CellStyleService cellStyleService) throws Exception {
        int rowNum = 0;
        if (dataList != null && dataList.size() > 0) {
            Class<?> cls = dataList.get(0).getClass();
            Method[] objMds = new Method[properties.length];
            int i = 0;
            for (String prop : properties) {
                objMds[i] = cls.getMethod(
                        "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1),
                        new Class[] {});
                i++;
            }
            rowNum = cellStyleService.buildSheetStyle(wb, sheet, dataList, objMds, rowNumStart);
        }
        return rowNum;
    }

    /**
     * @param os 输出流,方法内不会关闭流，请外面自行关闭
     * @param dataList 输出数据列表 ，数据对象要有public的get方法
     * @param defineMapping 数据定
     *            {{"XX商品名","getProductName","100"},{"XX商品ID","getProductID"
     *            ,"100"},{"表头名称","get对象属性","列宽"}...}
     * @return
     */
    public static Workbook defaultExport(OutputStream os, List<?> dataList,
                                         String[][] defineMapping, CellStyleService cellStyleService)
            throws Exception {

        // 创建book
        Workbook wb = new HSSFWorkbook();

        // 表头单元格边STYLE
        CellStyle headStyle = getDefautHeaderStyle(wb);
        // 数据 单元格STYLE
        //CellStyle dataStyle = getDefautDataStyle(wb);
        // 创建 包裹信息sheet
        Sheet sheet = wb.createSheet("default");

        String[] titles = new String[defineMapping.length];
        String[] properties = new String[defineMapping.length];
        String[] widths = new String[defineMapping.length];
        for (int i = 0; i < defineMapping.length; i++) {
            // 表头
            titles[i] = defineMapping[i][0];
            // 数据映射对象属性
            properties[i] = defineMapping[i][1];
            if (defineMapping[i].length > 2) {
                // 列宽
                widths[i] = defineMapping[i][2];
            }
        }

        // 判断是否设置了列宽
        if (defineMapping[0].length > 2) {
            for (int j = 0; j < widths.length; j++) {
                sheet.setColumnWidth(j, Integer.valueOf(widths[j]) * 38);
            }
        }

        // writeHeader
        writeRow(sheet, titles, headStyle, 0);

        // writeData
        writeRow(wb, sheet, dataList, properties, 1, cellStyleService);

        // 写入到输出流
        wb.write(os);

        return wb;
    }

    /**
     * 大批量(2w条以上)excel数据导出<br/>
     * 输出文件为Excel2007格式(扩展名xlsx)
     * 
     * @param os
     * @param defineMapping
     * @param cellStyleService
     * @param pageQueryCallback
     * @return
     * @throws Exception
     */
    public static Workbook largeDataExport(OutputStream os, String[][] defineMapping,
                                           CellStyleService cellStyleService,
                                           PageQueryCallback pageQueryCallback) throws Exception {
        // 创建一个Excel2007工作薄
        Workbook wb = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE);
        ((SXSSFWorkbook) wb).setCompressTempFiles(true);
        // 表头单元格边STYLE
        CellStyle headStyle = getDefautHeaderStyle(wb);
        // 创建一个默认的sheet
        Sheet sheet = wb.createSheet("default");
        String[] titles = new String[defineMapping.length];
        String[] fieldNames = new String[defineMapping.length];
        String[] widths = new String[defineMapping.length];
        for (int i = 0; i < defineMapping.length; i++) {
            // 表头
            titles[i] = defineMapping[i][0];
            // 数据映射对象属性
            fieldNames[i] = defineMapping[i][1];
            if (defineMapping[i].length > 2) {
                // 列宽
                widths[i] = defineMapping[i][2];
            }
        }

        // 判断是否设置了列宽
        if (defineMapping[0].length > 2) {
            for (int j = 0; j < widths.length; j++) {
                sheet.setColumnWidth(j, Integer.valueOf(widths[j]) * 38);
            }
        }

        // writeHeader
        writeRow(sheet, titles, headStyle, 0);

        // writeData
        int startRowNum = 1;
        int pageNo = 1;
        Page<?> page = pageQueryCallback.getPage(pageNo);
        while (page != null && !CollectionUtils.isEmpty(page.getDatas())) {
            startRowNum = writeRow(wb, sheet, page.getDatas(), fieldNames, startRowNum,
                    cellStyleService);
            pageNo++;
            page = pageQueryCallback.getPage(pageNo);
        }

        // 写入到输出流
        wb.write(os);

        return wb;
    }

    /**
     * 给格子设置值
     * 
     * @param cell
     * @param value
     */
    public static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Float) {
            //转成string，不然Excle显示的不一样
            cell.setCellValue(value.toString());
        } else if (value instanceof Double) {
            //转成string，不然Excle显示的会不一样
            cell.setCellValue(value.toString());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 创建头部的默认样式
     * 
     * @param wb
     * @return
     */
    public static CellStyle getDefautHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        // 线条
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setFillForegroundColor(IndexedColors.LIME.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 背景
        // style.setFillBackgroundColor(new HSSFColor.BLUE_GREY().getIndex());

        //字体
        Font titleFont = wb.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);

        return style;
    }

    /**
     * 设置默认数据部分格式
     * 
     * @param wb
     * @return
     */
    public static CellStyle getDefautDataStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        // 默认不设置
        return style;
    }

    /**
     * 取一个excel单元格的值
     * 
     * @author xiasq
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    
                    cellValue = StringUtils.trim(cell.getRichStringCellValue().getString());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = cell.getBooleanCellValue() ? "TRUE" : "FALSE";
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = cell.getDateCellValue().toString();
                    } else {
                        cellValue = NumberFormat.getNumberInstance()
                                .format(cell.getNumericCellValue()).replaceAll(",", "");
                    }
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cellValue = StringUtils.trim(cell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellValue = StringUtils.trim(String.valueOf(cell.getErrorCellValue()));
                    break;
                default:
                    cellValue = "";
            }
        }
        return cellValue;
    }

    /**
     * 获取扩展名
     * 
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }

    /**
     * 获取结果文件名
     * 
     * @param fileName
     * @return
     */
    public static String getResultFileName(String fileName) {
        int pos = fileName.lastIndexOf(".");
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
        String dateStr = format.format(new Date());
        if (pos > 0) {
            String newName = fileName.substring(0, pos) + RESULT_SUFFIX + dateStr
                    + fileName.substring(pos);
            return newName;
        } else {
            return fileName + RESULT_SUFFIX + dateStr;
        }

    }


    /**
     * 取Excel所有数据，包含header
     * 
     * @return List<String[]>
     */
    public static List<String[]> getAllData(Workbook wb, int sheetIndex) {
        List<String[]> dataList = new ArrayList<String[]>();
        int columnNum = 0;
        int startColNum = 0;
        int endColNum = 0;
        Sheet sheet = wb.getSheetAt(sheetIndex);
        if (sheet.getRow(0) != null) {
            startColNum = sheet.getRow(0).getFirstCellNum();
            endColNum = sheet.getRow(0).getLastCellNum();
            columnNum = endColNum - startColNum;
        }
        if (columnNum > 0) {
            for (Row row : sheet) {
                if (row == null) {
                    continue;
                }
                String[] singleRow = new String[columnNum];
                for (int i = 0; i < columnNum; i++) {
                    Cell cell = row.getCell(i + startColNum);
                    if (cell != null) {
                        singleRow[i] = getCellValue(cell);
                    }
                }
                dataList.add(singleRow);
            }
        }
        return dataList;
    }

    /**
     * 返回Excel最大行index值，实际行数要加1
     * 
     * @return
     */
    public static int getRowNum(Workbook wb, int sheetIndex) {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        return sheet.getLastRowNum();
    }

    /**
     * 返回数据的列数
     * 
     * @return
     */
    public static int getColumnNum(Workbook wb, int sheetIndex) {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row = sheet.getRow(0);
        if (row != null && row.getLastCellNum() > 0) {
            return row.getLastCellNum();
        }
        return 0;
    }

    /**
     * 类ExcelUtil.java的实现描述：导入导出返回对象
     * 
     * @author weirui.shenwr 2011-8-11
     */
    static public class ExcelIOResult {

        /**
         * 总数
         */
        public int     totalCount   = 0;
        /**
         * 成功数
         */
        public int     seccussCount = 0;
        /**
         * 失败数
         */
        public int     errorCount   = 0;

        /**
         * 输出文件文件名
         */
        private String resultFileName;

        public String getResultFileName() {
            return resultFileName;
        }

        public void setResultFileName(String resultFileName) {
            this.resultFileName = resultFileName;
        }

        /**
         * 返回错误的记录
         */
        private List<Object> errorResult = new ArrayList<Object>();

        public List<Object> getErrorResult() {
            return errorResult;
        }

        public void setErrorResult(List<Object> errorResult) {
            this.errorResult = errorResult;
        }

        public void addErrorResult(Object e) {
            errorResult.add(e);
        }

        /**
         * 错误码
         */
        private ResultCode resultCode = ResultCode.SUCCESS;

        public ResultCode getResultCode() {
            return resultCode;
        }

        public void setResultCode(ResultCode resultCode) {
            this.resultCode = resultCode;
        }

        /**
         * 消息列表
         */
        private List<String> messageList = new ArrayList<String>();

        public List<String> getMessageList() {
            return messageList;
        }

        public void setMessageList(List<String> messageList) {
            this.messageList = messageList;
        }

        /**
         * 增加一条消息
         * 
         * @param msg
         */
        public void addMsg(String msg) {
            messageList.add(msg);
        }

        /**
         * 是否成功
         * 
         * @return
         */
        public boolean isSuccess() {
            if (resultCode == ResultCode.SUCCESS) {
                return true;
            }
            return false;
        }

        /**
         * 是否验证通过
         * 
         * @return
         */
        public boolean isNotValid() {
            if (resultCode == ResultCode.NOT_VALID) {
                return true;
            }
            return false;
        }

        /**
         * 获取处理的概要消息
         * 
         * @return
         */
        public String getSummaryMsg() {
            if (totalCount == 0) {
                return "没有可处理的记录！";
            }
            return MessageFormat.format("共上传{0}条数据，成功{1}条，错误{2}条", totalCount, seccussCount,
                    errorCount);
        }
    }

    /**
     * 返回CODE枚举类
     * 
     * @author weirui.shenwr 2011-8-11
     */
    static public enum ResultCode {
        SUCCESS,
        NOT_VALID,
        DATA_ERROR,
        EXCEPTION
    }

    /**
     * 文件限制定义
     * 
     * @author weirui.shenwr 2011-8-11
     */
    static public class LimitDefine {

        /**
         * 文件最大大小 默认2M
         */
        private Long maxSize = Long.valueOf(2 * 1024 * 1024);
        /**
         * 默认导入的最大行数,默认2000
         */
        private int  maxRows = 2000;

        public Long getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Long maxSize) {
            this.maxSize = maxSize;
        }

        public int getMaxRows() {
            return maxRows;
        }

        public void setMaxRows(int maxRows) {
            this.maxRows = maxRows;
        }

    }
    
}
