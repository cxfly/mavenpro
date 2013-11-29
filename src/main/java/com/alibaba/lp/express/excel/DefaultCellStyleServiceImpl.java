package com.alibaba.lp.express.excel;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DefaultCellStyleServiceImpl implements CellStyleService {

    @Override
    public int buildSheetStyle(Workbook wb, Sheet sheet, List<?> dataList, Method[] objMds, int rowNumStart)
                                                                                                            throws Exception {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        // 自动换行
        style.setWrapText(true);
        // 顶部对齐
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setAlignment(CellStyle.ALIGN_LEFT);

        for (Object dataObj : dataList) { // 遍历每一行
            Row row = sheet.createRow(rowNumStart);
            rowNumStart++;
            for (int i = 0; i < objMds.length; i++) { // 处理每一列
                Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                Object value = objMds[i].invoke(dataObj);
                cell.setCellStyle(style);
                ExcelUtil.setCellValue(cell, value);
            }
        }
        return rowNumStart;
    }
}
