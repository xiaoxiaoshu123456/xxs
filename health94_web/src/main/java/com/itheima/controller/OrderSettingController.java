package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("importOrderSettings")
    public Result importOrderSettings(@RequestParam("excelFile")MultipartFile file){

        try {
            //调用工具类获取模板内容
            List<String[]> strings = POIUtils.readExcel(file);

            //定义List，封装所有的OrderSetting
            List<OrderSetting> orderSettings = new ArrayList<>();
            if (strings!=null){

                for (String[] string : strings) {
                    //将每一个String数组封装成一个个的OrderSetting对象
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(DateUtils.parseString2Date(string[0],"yyyy/MM/dd"));
                    orderSetting.setNumber(Integer.parseInt(string[1]));

                    orderSettings.add(orderSetting);
                }
                orderSettingService.importOrderSettings(orderSettings);

            }

            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }

    /**
     * 查询可预约数据展示到日历中
     */
    @RequestMapping("findOrderSettingByMonth")
    public Result findOrderSettingByMonth(@RequestParam("date")String date){

        try {
            List<Map> list = orderSettingService.findOrderSettingByMonth(date);
            return new Result(true,MessageConstant.QUERY_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDERSETTING_FAIL);
        }
    }

    /**
     * 设置可预约数量
     */
    @RequestMapping("updateNumberByOrderDate")
    public Result updateNumberByOrderDate(@RequestBody OrderSetting orderSetting){

        try {
            orderSettingService.updateNumberByOrderDate(orderSetting);
            return new Result(true,MessageConstant.EDIT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ORDERSETTING_FAIL);
        }
    }
}
