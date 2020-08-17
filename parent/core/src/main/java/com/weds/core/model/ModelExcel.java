package com.weds.core.model;

import com.weds.core.utils.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ModelExcel {
    public static void excelrw(Map<String, List<ModelEntity>> ls, String path) throws Exception {
        OutputStream out = null;
        Workbook workbook = new HSSFWorkbook();
        int i = 1;
        // 样试设置
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直方向上居中对齐
        style.setFont(font);

        CellStyle style2 = workbook.createCellStyle();
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);// 水平方向上居中对齐
        style2.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直方向上居中对齐

        CellStyle style3 = workbook.createCellStyle();
        style3.setBorderLeft(BorderStyle.DOTTED);
        style3.setBorderRight(BorderStyle.DOTTED);
        style3.setBorderBottom(BorderStyle.DOTTED);

        // 设置单元格背景色
        CellStyle style4 = workbook.createCellStyle();
        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style4.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);

        Sheet idxSheet = workbook.createSheet();
        workbook.setSheetName(0, "Index");

        Row row = idxSheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("表空间");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("索引空间");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("删除语句");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("0");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("数据库类型");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("MSSQL");
        cell.setCellStyle(style);

        row = idxSheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("库表中文名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("库表名称");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("Sheet名称");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("备注");
        cell.setCellStyle(style);

        int[] indexWidth = {8, 20, 20, 20, 20, 10, 20, 10};
        for (int j = 0; j < indexWidth.length; j++) {
            idxSheet.setColumnWidth(j, indexWidth[j] * 256);
        }

        // idxSheet.autoSizeColumn(0);

        int[] sheetWidth = {5, 15, 15, 12, 5, 5, 7, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 10, 10, 15, 8};
        try {
            for (Map.Entry<String, List<ModelEntity>> entry : ls.entrySet()) {
                int iRow = 8;
                Sheet sheet = workbook.createSheet();
                String name = entry.getKey().split(",")[0];
                String desc = entry.getKey().split(",")[1];

                // if (desc.equals("null")) {
                workbook.setSheetName(i, name.toUpperCase());
                // } else {
                // workbook.setSheetName(i, desc.toUpperCase());
                // }

                // 第2行
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 6));
                row = sheet.createRow(1);
                cell = row.createCell(1);
                cell.setCellValue("描述");
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellValue(desc.equals("null") ? "" : desc);
                cell.setCellStyle(style2);
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell = row.createCell(5);
                cell.setCellStyle(style);
                cell = row.createCell(6);
                cell.setCellStyle(style);
                cell = row.createCell(7);
                cell.setCellValue("SCHEMA");

                // 第3行
                row = sheet.createRow(2);
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 6));
                cell = row.createCell(1);
                cell.setCellValue("表名");
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellValue(entry.getKey().split(",")[0].toUpperCase());
                cell.setCellStyle(style2);
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell = row.createCell(5);
                cell.setCellStyle(style);
                cell = row.createCell(6);
                cell.setCellStyle(style);
                // 第4行
                sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 6));
                row = sheet.createRow(3);
                cell = row.createCell(1);
                cell.setCellValue("备注");
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellStyle(style2);
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell = row.createCell(5);
                cell.setCellStyle(style);
                cell = row.createCell(6);
                cell.setCellStyle(style);
                // 第5行
                sheet.createRow(4);
                // 第6行
                row = sheet.createRow(5);
                cell = row.createCell(0);
                cell.setCellValue(1);
                cell = row.createCell(1);
                cell.setCellValue(2);
                cell = row.createCell(2);
                cell.setCellValue(3);
                cell = row.createCell(3);
                cell.setCellValue(4);
                cell = row.createCell(4);
                cell.setCellValue(5);
                cell = row.createCell(5);
                cell.setCellValue(6);
                cell = row.createCell(6);
                cell.setCellValue(7);
                cell = row.createCell(7);
                cell.setCellValue(8);
                cell = row.createCell(8);
                cell.setCellValue(9);
                cell = row.createCell(9);
                cell.setCellValue(10);
                cell = row.createCell(10);
                cell.setCellValue(11);
                cell = row.createCell(11);
                cell.setCellValue(12);
                cell = row.createCell(12);
                cell.setCellValue(13);
                cell = row.createCell(13);
                cell.setCellValue(14);
                cell = row.createCell(14);
                cell.setCellValue(15);
                cell = row.createCell(15);
                cell.setCellValue(16);
                cell = row.createCell(16);
                cell.setCellValue(17);
                cell = row.createCell(17);
                cell.setCellValue(18);
                cell = row.createCell(18);
                cell.setCellValue(19);
                cell = row.createCell(19);
                cell.setCellValue(20);
                cell = row.createCell(20);
                cell.setCellValue(21);
                cell = row.createCell(21);
                cell.setCellValue(22);
                cell = row.createCell(22);
                cell.setCellValue(23);
                cell = row.createCell(23);
                cell.setCellValue(24);
                cell = row.createCell(24);
                cell.setCellValue(25);
                cell = row.createCell(25);
                cell.setCellValue(26);
                cell = row.createCell(26);
                cell.setCellValue(27);
                cell = row.createCell(27);
                cell.setCellValue(28);
                // 第7行
                row = sheet.createRow(6);
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 5, 5));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 6, 6));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 7, 7));
                sheet.addMergedRegion(new CellRangeAddress(6, 6, 8, 18));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 19, 19));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 20, 20));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 21, 21));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 22, 22));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 23, 23));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 24, 24));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 25, 25));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 26, 26));
                sheet.addMergedRegion(new CellRangeAddress(6, 7, 27, 27));
                cell = row.createCell(0);
                cell.setCellValue("#");
                cell.setCellStyle(style);
                cell = row.createCell(1);
                cell.setCellValue("项目名");
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellValue("项目ID");
                cell.setCellStyle(style);
                cell = row.createCell(3);
                cell.setCellValue("类型");
                cell.setCellStyle(style);
                cell = row.createCell(4);
                cell.setCellValue("长度");
                cell.setCellStyle(style);
                cell = row.createCell(5);
                cell.setCellValue("精度");
                cell.setCellStyle(style);
                cell = row.createCell(6);
                cell.setCellValue("字节数");
                cell.setCellStyle(style);
                cell = row.createCell(7);
                cell.setCellValue("NOT NULL");
                cell.setCellStyle(style);
                cell = row.createCell(8);
                cell.setCellValue("KEY/索引");
                cell.setCellStyle(style);
                cell = row.createCell(9);
                cell.setCellStyle(style);
                cell = row.createCell(10);
                cell.setCellStyle(style);
                cell = row.createCell(11);
                cell.setCellStyle(style);
                cell = row.createCell(12);
                cell.setCellStyle(style);
                cell = row.createCell(13);
                cell.setCellStyle(style);
                cell = row.createCell(14);
                cell.setCellStyle(style);
                cell = row.createCell(15);
                cell.setCellStyle(style);
                cell = row.createCell(16);
                cell.setCellStyle(style);
                cell = row.createCell(17);
                cell.setCellStyle(style);
                cell = row.createCell(18);
                cell.setCellStyle(style);
                cell = row.createCell(19);
                cell.setCellValue("内容概要");
                cell.setCellStyle(style);
                cell = row.createCell(20);
                cell.setCellValue("代码参照");
                cell.setCellStyle(style);
                cell = row.createCell(21);
                cell.setCellValue("备注");
                cell.setCellStyle(style);
                cell = row.createCell(22);
                cell.setCellValue("默认值");
                cell.setCellStyle(style);
                cell = row.createCell(23);
                cell.setCellValue("列表");
                cell.setCellStyle(style);
                cell = row.createCell(24);
                cell.setCellValue("表单");
                cell.setCellStyle(style);
                cell = row.createCell(25);
                cell.setCellValue("查询");
                cell.setCellStyle(style);
                cell = row.createCell(26);
                cell.setCellValue("类型");
                cell.setCellStyle(style);
                cell = row.createCell(27);
                cell.setCellValue("字典");
                cell.setCellStyle(style);
                // 第8行
                row = sheet.createRow(7);
                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell = row.createCell(5);
                cell.setCellStyle(style);
                cell = row.createCell(6);
                cell.setCellStyle(style);
                cell = row.createCell(7);
                cell.setCellStyle(style);
                cell = row.createCell(8);
                cell.setCellValue("PK");
                cell.setCellStyle(style);
                cell = row.createCell(9);
                cell.setCellValue("1");
                cell.setCellStyle(style);
                cell = row.createCell(10);
                cell.setCellValue("2");
                cell.setCellStyle(style);
                cell = row.createCell(11);
                cell.setCellValue("3");
                cell.setCellStyle(style);
                cell = row.createCell(12);
                cell.setCellValue("4");
                cell.setCellStyle(style);
                cell = row.createCell(13);
                cell.setCellValue("5");
                cell.setCellStyle(style);
                cell = row.createCell(14);
                cell.setCellValue("6");
                cell.setCellStyle(style);
                cell = row.createCell(15);
                cell.setCellValue("7");
                cell.setCellStyle(style);
                cell = row.createCell(16);
                cell.setCellValue("8");
                cell.setCellStyle(style);
                cell = row.createCell(17);
                cell.setCellStyle(style);
                cell.setCellValue("9");
                cell.setCellStyle(style);
                cell = row.createCell(18);
                cell.setCellStyle(style);
                cell.setCellValue("10");
                cell.setCellStyle(style);
                cell = row.createCell(19);
                cell.setCellStyle(style);
                cell = row.createCell(20);
                cell.setCellStyle(style);
                cell = row.createCell(21);
                cell.setCellStyle(style);
                cell = row.createCell(22);
                cell.setCellStyle(style);
                cell = row.createCell(23);
                cell.setCellStyle(style);
                cell = row.createCell(24);
                cell.setCellStyle(style);
                cell = row.createCell(25);
                cell.setCellStyle(style);
                cell = row.createCell(26);
                cell.setCellStyle(style);
                cell = row.createCell(27);
                cell.setCellStyle(style);

                List<ModelEntity> tab = entry.getValue();
                for (int y = 0; y < tab.size(); y++) {
                    row = sheet.createRow(iRow);
                    cell = row.createCell(0);
                    cell.setCellValue(y + 1);
                    cell.setCellStyle(style3);
                    cell = row.createCell(1);
                    String cnName = tab.get(y).getCnname();
                    if (!StringUtils.isBlank(cnName) && cnName.contains("#")) {
                        cnName = cnName.replace("##", "# #") + " ";
                        cell.setCellValue(cnName.split("#")[0].trim());
                    } else {
                        cell.setCellValue(cnName);
                    }
                    cell.setCellStyle(style3);
                    cell = row.createCell(2);
                    cell.setCellValue(tab.get(y).getEnname().toUpperCase());
                    cell.setCellStyle(style3);
                    cell = row.createCell(3);
                    cell.setCellValue(tab.get(y).getSqltype().toUpperCase());
                    cell.setCellStyle(style3);
                    cell = row.createCell(4);
                    if (tab.get(y).getSize() == 0)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getSize());
                    cell.setCellStyle(style3);
                    cell = row.createCell(5);
                    if (tab.get(y).getJingdu() == 0)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getJingdu());
                    cell.setCellStyle(style3);

                    cell = row.createCell(6);
                    cell.setCellStyle(style3);
                    cell = row.createCell(7);
                    if (tab.get(y).getIsnull() == 0)
                        cell.setCellValue("O");
                    // 主键
                    cell.setCellStyle(style3);
                    cell = row.createCell(8);
                    if (tab.get(y).getIspk() == 0)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIspk());
                    // 索引0
                    cell.setCellStyle(style3);
                    cell = row.createCell(9);
                    if (tab.get(y).getIssy0() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy0());
                    // 索引1
                    cell.setCellStyle(style3);
                    cell = row.createCell(10);
                    if (tab.get(y).getIssy1() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy1());
                    // 索引2
                    cell.setCellStyle(style3);
                    cell = row.createCell(11);
                    if (tab.get(y).getIssy2() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy2());
                    // 索引3
                    cell.setCellStyle(style3);
                    cell = row.createCell(12);
                    if (tab.get(y).getIssy3() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy3());
                    // 索引4
                    cell.setCellStyle(style3);
                    cell = row.createCell(13);
                    if (tab.get(y).getIssy4() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy4());
                    // 索引5
                    cell.setCellStyle(style3);
                    cell = row.createCell(14);
                    if (tab.get(y).getIssy5() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy5());
                    // 索引6
                    cell.setCellStyle(style3);
                    cell = row.createCell(15);
                    if (tab.get(y).getIssy6() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy6());
                    // 索引7
                    cell.setCellStyle(style3);
                    cell = row.createCell(16);
                    if (tab.get(y).getIssy7() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy7());
                    // 索引8
                    cell.setCellStyle(style3);
                    cell = row.createCell(17);
                    if (tab.get(y).getIssy8() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy8());
                    // 索引9
                    cell.setCellStyle(style3);
                    cell = row.createCell(18);
                    if (tab.get(y).getIssy9() == -1)
                        cell.setCellValue("");
                    else
                        cell.setCellValue(tab.get(y).getIssy9());
                    // 备注
                    cell.setCellStyle(style3);
                    cell = row.createCell(19);
                    cell.setCellStyle(style3);
                    cell = row.createCell(20);
                    cell.setCellStyle(style3);
                    cell = row.createCell(21);
                    cell.setCellStyle(style3);
                    cell = row.createCell(22);
                    cell.setCellStyle(style3);

                    cell = row.createCell(23);
                    if (!StringUtils.isBlank(cnName) && cnName.contains("#")) {
                        cell.setCellValue(cnName.split("#")[1].trim());
                    }
                    cell.setCellStyle(style3);
                    cell = row.createCell(24);
                    if (!StringUtils.isBlank(cnName) && cnName.contains("#")) {
                        cell.setCellValue(cnName.split("#")[2].trim());
                    }
                    cell.setCellStyle(style3);
                    cell = row.createCell(25);
                    if (!StringUtils.isBlank(cnName) && cnName.contains("#")) {
                        cell.setCellValue(cnName.split("#")[3].trim());
                    }
                    cell.setCellStyle(style3);
                    cell = row.createCell(26);
                    if (!StringUtils.isBlank(cnName) && cnName.contains("#")) {
                        cell.setCellValue(cnName.split("#")[4].trim());
                    }
                    cell.setCellStyle(style3);
                    cell = row.createCell(27);
                    if (!StringUtils.isBlank(cnName) && cnName.contains("#")) {
                        cell.setCellValue(cnName.split("#")[5].trim());
                    }
                    cell.setCellStyle(style3);
                    iRow++;
                }

                for (int j = 0; j < sheetWidth.length; j++) {
                    sheet.setColumnWidth(j, sheetWidth[j] * 256);
                }
                i++;
            }

            out = new FileOutputStream(path);
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            out.flush();
            out.close();
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
