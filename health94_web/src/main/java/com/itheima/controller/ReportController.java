package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    @RequestMapping("getMemberReport")
    public Result getMemberReport(){

        try {
            Map map = memberService.getMemberReport();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }

    /**
     * 查询套餐占比数据
     */
    @RequestMapping("getSetmealReport")
    public Result getSetmealReport(){

        try {
            Map map = setmealService.getSetmealReport();
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    /**
     * 运营数据统计
     */
    @RequestMapping("getBusinessReport")
    public Result getBusinessReport(){

        try {
            Map map =  reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     *
     * reportDate:null,//报告日期
     *     todayNewMember :0,//今日新增会员数
     *     totalMember :0,//总会员数
     *     thisWeekNewMember :0,//本周新增会员数
     *     thisMonthNewMember :0,//本月新增会员数
     *     todayOrderNumber :0,//今日预约数
     *     todayVisitsNumber :0,//今日到诊数
     *     thisWeekOrderNumber :0,//本周预约数
     *     thisWeekVisitsNumber :0,//本周到诊数
     *     thisMonthOrderNumber :0,//本月预约数
     *     thisMonthVisitsNumber :0,//本月到诊数
     *     hotSetmeal ://显示4个热门套餐
     *
     * 将运营数据统计的excelt模板响应给浏览器，让其以附件形式进行下载。
     */
    @RequestMapping("exportBusinessReport")
    public void exportBusinessReport(HttpSession session, HttpServletResponse response){

        try {


            //获取运营数据
            Map result = reportService.getBusinessReport();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String path = session.getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //加载excel模板，获取工作簿对象
            XSSFWorkbook workbook = new XSSFWorkbook(path);
            //获取工作表实例
            XSSFSheet sheet = workbook.getSheetAt(0);
            //通过工作表获取row,行
            XSSFRow row = sheet.getRow(2);
            //通过行row获取单元格，填充数据
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);


            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);


            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);


            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            int num = 12;
            for (Map map : hotSetmeal) {

                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");

                row = sheet.getRow(num);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.toString());

                num++;
            }


            ServletOutputStream outputStream = response.getOutputStream();

            //设置文件响应格式
            response.setContentType("application/vnd.ms-excel");
            //设置响应头，通知浏览器以附件形式下载excel
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");

            workbook.write(outputStream);
            //释放资源
            outputStream.flush();
            outputStream.close();
            workbook.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
