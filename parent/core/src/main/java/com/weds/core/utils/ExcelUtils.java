package com.weds.core.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    // 设置单元格样式边框为：上、下、左、右粗线
    public final static int CELL_STYLE_01 = 0;
    // 设置单元格样式边框为：上、下、左、右细线
    public final static int CELL_STYLE_02 = 1;
    // 设置单元格样式边框为：上、下、左、右虚线
    public final static int CELL_STYLE_03 = 2;

    // 设置单元格居左
    public final static int ALIGN_STYLE_01 = 0;
    // 设置单元格居中
    public final static int ALIGN_STYLE_02 = 1;
    // 设置单元格居右
    public final static int ALIGN_STYLE_03 = 2;

    public static final String EXCEL_TYPE_2007 = "xlsx";

    public static final String EXCEL_TYPE_2003 = "xls";

    private String type;

    public ExcelUtils() {
        this.type = EXCEL_TYPE_2007;
    }

    public ExcelUtils(String type) {
        this.type = type;
    }

    private Workbook workbook = null;

    private Sheet worksheet = null;

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getWorksheet() {
        return worksheet;
    }

    /**
     * @param is
     * @return
     * @throws IOException
     */
    public Workbook readWorkBook(InputStream is) throws IOException {
        Workbook workbook;
        if (EXCEL_TYPE_2007.equalsIgnoreCase(this.type)) {
            workbook = new XSSFWorkbook(is);
        } else {
            workbook = new HSSFWorkbook(is);
        }
        return workbook;
    }

    /**
     * @param is
     * @param sheetVal
     * @return
     * @throws IOException
     */
    public Sheet readWorkSheet(InputStream is, Object sheetVal) throws IOException {
        Sheet sheet;
        Workbook workbook = readWorkBook(is);
        if (sheetVal instanceof String) {
            sheet = workbook.getSheet(sheetVal.toString());
        } else {
            sheet = workbook.getSheetAt(Integer.parseInt(sheetVal.toString()));
        }
        return sheet;
    }

    public List<Map> readWorkSheetValue(InputStream is, Object sheetVal, int startRow) throws IOException {
        Sheet sheet = readWorkSheet(is, sheetVal);
        return readWorkSheetValue(sheet, startRow);
    }

    public List<Map> readWorkSheetValue(InputStream is, Object sheetVal) throws IOException {
        Sheet sheet = readWorkSheet(is, sheetVal);
        return readWorkSheetValue(sheet, 0);
    }

    public List<Map> readWorkSheetValue(Sheet sheet, int startRow) {
        int firstRow = Math.max(sheet.getFirstRowNum(), startRow);
        int allRowNum = sheet.getLastRowNum();
        List<Map> list = new ArrayList<>();
        for (int i = firstRow; i < allRowNum; i++) {
            Row row = sheet.getRow(i);
            int firstCellNum = row.getFirstCellNum();
            int allColNum = row.getPhysicalNumberOfCells();
            Map<String, String> map = new HashMap<>();
            for (int j = firstCellNum; j < allColNum; j++) {
                String value = readCellValue(row.getCell(j));
                map.put("col" + j, value);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * @param cell
     * @return
     */
    public String readCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况  
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型  
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字  
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串  
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean  
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式  
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值   
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障  
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    /**
     *
     */
    public void createWorkBook() {
        if (EXCEL_TYPE_2007.equalsIgnoreCase(this.type)) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
    }

    /**
     * @param sheetName
     */
    public void createWorkSheet(String sheetName) {
        worksheet = workbook.createSheet(sheetName);
    }

    /**
     * @param rowidx
     * @return
     */
    public Row createRow(int rowidx) {
        Row row = worksheet.getRow(rowidx);
        if (row == null) {
            row = worksheet.createRow(rowidx);
        }
        return row;
    }

    public Cell createCell(int colidx, int rowidx) {
        Row row = createRow(rowidx);
        Cell cell = row.getCell(colidx);
        if (cell == null) {
            cell = row.createCell(colidx);
        }
        return cell;
    }

    /**
     * @param colidx
     * @param rowidx
     * @param text
     * @param cellStyle
     * @throws Exception
     */
    public void addText(int colidx, int rowidx, Object text, CellStyle cellStyle) throws Exception {
        // 如果行数小于0 行数等于0
        if (rowidx < 0) {
            rowidx = 0;
        }
        // 如果列数小于0，列数等于0
        if (colidx < 0) {
            colidx = 0;
        }

        Row row = worksheet.getRow(rowidx);
        if (row == null) {
            row = worksheet.createRow(rowidx);
        }

        Cell cell = row.getCell(colidx);
        if (cell == null) {
            cell = row.createCell(colidx);
        }

        if (text == null) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue("");
        } else {
            if (text instanceof Integer || text instanceof Double || text instanceof Float || text instanceof Long) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(Double.valueOf(text.toString()));
            } else if (text instanceof String) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(text.toString());
            } else {
                throw new Exception("数据类型错误");
            }
        }

        if (null != cellStyle) {
            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * @param colidx
     * @param rowidx
     * @param text
     * @throws Exception
     */
    public void addText(int colidx, int rowidx, Object text) throws Exception {
        addText(colidx, rowidx, text, null);
    }

    /**
     * @param datas
     * @param keys
     * @param dataStyle
     * @param titles
     * @param titleStyle
     * @throws Exception
     */
    public void addText(List<Map<?, ?>> datas, List<String> keys, CellStyle dataStyle, List<String> titles,
                        CellStyle titleStyle) throws Exception {
        int rowidx = 0;
        if (titles != null) {
            for (int i = 0; i < titles.size(); i++) {
                addText(i, rowidx, titles.get(i), titleStyle);
            }
            rowidx++;
        }

        for (int i = 0; i < datas.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                Object obj = ((Map<?, ?>) datas.get(i)).get(keys.get(j));
                addText(j, rowidx + i, obj, dataStyle);
            }
        }
    }

    /**
     * @param datas
     * @param keys
     * @throws Exception
     */
    public void addText(List<Map<?, ?>> datas, List<String> keys) throws Exception {
        addText(datas, keys, null, null, null);
    }

    /**
     * @param datas
     * @param keys
     * @param titles
     * @throws Exception
     */
    public void addText(List<Map<?, ?>> datas, List<String> keys, List<String> titles) throws Exception {
        addText(datas, keys, null, titles, null);
    }

    /**
     * @param startCol
     * @param startRow
     * @param endCol
     * @param endRow
     */
    public void mergeCells(int startCol, int startRow, int endCol, int endRow) {
        CellRangeAddress region = new CellRangeAddress(startRow, endRow, startCol, endCol);
        worksheet.addMergedRegion(region);
    }

    /**
     * @param col
     * @param row
     */
    public void createFreezePane(int col, int row) {
        // 如果行数小于0 行数等于0
        if (row < 0) {
            row = 1;
        }
        // 如果列数小于0，列数等于0
        if (col < 0) {
            col = 0;
        }
        // 冻结单元格
        worksheet.createFreezePane(col, row);
    }

    /**
     * @return
     */
    public Font createFont() {
        return workbook.createFont();
    }

    /**
     * @return
     */
    public CellStyle createCellStyle() {
        return workbook.createCellStyle();
    }

    /**
     * @param type
     * @param align
     * @return
     */
    public CellStyle createCellStyle(int type, int align) {
        return createCellStyle(type, align, null, null);
    }

    /**
     * @param type
     * @param align
     * @param font
     * @return
     */
    public CellStyle createCellStyle(int type, int align, Font font) {
        return createCellStyle(type, align, font, null);
    }

    /**
     * @param type
     * @param align
     * @return
     */
    public CellStyle createCellStyle(int type, int align, Font font, Color color) {
        CellStyle cellStyle = createCellStyle();
        switch (type) {
            case CELL_STYLE_01:
                // 设置单元格样式边框为：上、下、左、右粗线
                cellStyle.setBorderTop(BorderStyle.MEDIUM);
                cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                cellStyle.setBorderRight(BorderStyle.MEDIUM);
                break;
            case CELL_STYLE_02:
                // 设置单元格样式边框为：上、下、左、右细线
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                break;
            case CELL_STYLE_03:
                // 设置单元格样式边框为：上、下、左、右虚线
                cellStyle.setBorderTop(BorderStyle.DOTTED);
                cellStyle.setBorderBottom(BorderStyle.DOTTED);
                cellStyle.setBorderLeft(BorderStyle.DOTTED);
                cellStyle.setBorderRight(BorderStyle.DOTTED);
                break;
            default:
                break;
        }

        switch (align) {
            case ALIGN_STYLE_01:
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                break;
            case ALIGN_STYLE_02:
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                break;
            case ALIGN_STYLE_03:
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                break;
            default:
                break;
        }

        if (font != null) {
            cellStyle.setFont(font);
        }

        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    public CellStyle createCellStyle(int type) {
        return createCellStyle(type, CELL_STYLE_02);
    }

    /**
     * @param fileName
     * @throws IOException
     */
    public void getExcelFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fiOut = new FileOutputStream(fileName);
        workbook.write(fiOut);
        fiOut.flush();
        fiOut.close();
    }

    /**
     * 取得Excel所有sheet的编号和名称
     *
     * @param filepath 文件路径
     * @return sheet个数(int型)
     * @throws Exception
     * @throws IOException
     */
    public Map<Integer, String> readSheetNum(String filepath) throws Exception,
            IOException {
        Map<Integer, String> sheetMap = new HashMap<Integer, String>();
        Workbook workbook;
        if (EXCEL_TYPE_2007.equalsIgnoreCase(this.type)) {
            workbook = new XSSFWorkbook(new FileInputStream(filepath));
        } else {
            workbook = new HSSFWorkbook(new FileInputStream(filepath));
        }
        Integer sheetNum = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i++) {
            sheetMap.put(sheetNum, workbook.getSheetName(i));
        }
        return sheetMap;
    }

    public void setColWidth(int colnum, int width) {
        if (width < 0) {
            worksheet.autoSizeColumn(colnum);
        } else {
            worksheet.setColumnWidth(colnum, width * 256);
        }
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public void deleteFiles(File file) {
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File f : files) {
            if (f.isFile()) {
                f.delete();
            } else {
                deleteFiles(f);
                f.delete();
            }
        }
    }
}