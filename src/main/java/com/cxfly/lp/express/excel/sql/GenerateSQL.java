package com.cxfly.lp.express.excel.sql;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GenerateSQL {

    static String sql = "update wl_trade t set t.gmt_modified = sysdate, t.receiver_name = '%s', t.receiver_prov = '%s', t.receiver_city = '%s', t.receiver_area = '%s', t.receiver_address = '%s', t.receiver_post = '%s',t.receiver_phone = '%s', t.receiver_mobile = '%s' where t.trade_id = '%s';";

    public static void main(String[] args) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("g:/tmp/modifyAddress.sql"));
        Workbook wb = WorkbookFactory.create(new FileInputStream("g:/tmp/地址订正（全部汇总）.xlsx"));
        //        Workbook wb = parse("g:/tmp/天猫魔盒地址订正汇总.xlsx");

        Sheet sheet = wb.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            int idx = 0;
            String A_tid = getCellVal(row.getCell(idx++));
            String B_name = getCellVal(row.getCell(idx++));
            String C_provice = getCellVal(row.getCell(idx++));
            String D_city = getCellVal(row.getCell(idx++));
            String E_area = getCellVal(row.getCell(idx++));
            String F_addr = getCellVal(row.getCell(idx++));
            String G_postCode = getCellVal(row.getCell(idx++));
            String H_phone = getCellVal(row.getCell(idx++));
            String I_mobile = getCellVal(row.getCell(idx++));
            String sqlLine = String.format(sql, B_name, C_provice, D_city, E_area, F_addr,
                    G_postCode, H_phone, I_mobile, A_tid);
            //            System.out.println(sqlLine);

            writer.write(sqlLine);
            writer.newLine();
        }

        writer.close();
    }

    @SuppressWarnings("unused")
    private static Workbook parse(String file) throws Exception {
        OPCPackage pkg = OPCPackage.open(new FileInputStream(file));
        XSSFWorkbook xssfwb = new XSSFWorkbook(pkg);
        Workbook wb = new SXSSFWorkbook(xssfwb, 100);
        return wb;
    }

    public static String getCellVal(Cell cell) {
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
}
