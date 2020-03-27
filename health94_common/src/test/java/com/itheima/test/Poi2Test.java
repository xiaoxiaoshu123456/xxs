package com.itheima.test;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

public class Poi2Test {
    @Test
    public void demo1() throws IOException {
        //创建工作工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");

        //获取工作表,既可以根据工作表的顺序获取,也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheet) {

            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获取单元格中的值
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }

        workbook.close();
    }



    @Test
    public void demo2() throws IOException {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("F:\\hello.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取当前工作表最后一行的行号，行号从0开始
        int lastRowNum = sheet.getLastRowNum();
        for(int i=0;i<=lastRowNum;i++){
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            //获取当前行的最后一个单元格的索引号
            short lastCellNum = row.getLastCellNum();
            //遍历每一行上的每一个单元格
            for(short j=0;j<lastCellNum;j++){
                String value = row.getCell(j).getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }
}
