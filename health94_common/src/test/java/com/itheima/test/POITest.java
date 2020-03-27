package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {
    @Test
    public void readExcel1() throws IOException {

        //读取excel，创建工作簿实例
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
        //获取工作表sheet
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取工作表中所有的数据行row
        for (Row row : sheet) {
            //遍历每一行中的每个单元格
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }
        //释放资源
        workbook.close();

    }

    @Test
    public void readExcel2() throws IOException {
        //读取excel，创建工作簿实例
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
        //获取工作表sheet
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取sheet中最后一行数据的角标
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            //获取行中的最后一个单元格的角标
            short lastCellNum = row.getLastCellNum();

            //循环遍历行中的每一个单元格
            for (short j = 0; j < lastCellNum; j++) {

                XSSFCell cell = row.getCell(j);

                System.out.println(cell.getStringCellValue());
            }

        }

        //释放资源
        workbook.close();

    }

    @Test
    public void writer() throws Exception {
        //创建一个工作簿实例
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建一个工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建行row
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("年龄");
        row.createCell(2).setCellValue("性别");
        //循环添加10行数据
        for (int i = 1; i <= 10; i++) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue("admin"+i);
            row.createCell(1).setCellValue(18+i);
            row.createCell(2).setCellValue(i%2==0?"男":"女");
        }

        FileOutputStream outputStream = new FileOutputStream(new File("d:\\itcast.xlsx"));
        workbook.write(outputStream);
        //释放资源
        outputStream.flush();//刷出缓冲区
        outputStream.close();
        workbook.close();

    }

}
