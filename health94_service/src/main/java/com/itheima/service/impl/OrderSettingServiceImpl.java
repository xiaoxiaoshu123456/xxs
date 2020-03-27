package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.OrderSettingMapper;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Override
    public void importOrderSettings(List<OrderSetting> orderSettings) {

        //判断保存到数据库的日期之前有没有保存过
        for (OrderSetting orderSetting : orderSettings) {
            //通过日期到数据库查询有没有保存过
            Integer count = orderSettingMapper.findByOrderDate(orderSetting.getOrderDate());
            if (count>0){
                //如果保存过，那么就执行修改
                orderSettingMapper.updateNumberByOrderDate(orderSetting);
            }else{
                //如果没有保存过，那么就执行新增
                orderSettingMapper.add(orderSetting);
            }

        }

    }

    @Override
    public List<Map> findOrderSettingByMonth(String date) {
        //定义查询的起始值
        String dateBegin = date+"-01";
        //定义查询的结束值
        String dateEnd = date+"-31";

        //执行查询
        List<Map> orderSettings = orderSettingMapper.findOrderSettingByMonth(dateBegin,dateEnd);

        return orderSettings;
    }

    /*@Override
    public List<Map> findOrderSettingByMonth(String date) {

        //定义查询的起始值
        String dateBegin = date+"-01";
        //定义查询的结束值
        String dateEnd = date+"-31";

        //执行查询
        List<OrderSetting> orderSettings = orderSettingMapper.findOrderSettingByMonth(dateBegin,dateEnd);

        //定义一个list封装多个map
        List<Map> list = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            Map<String, Integer> map = new HashMap<>();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            list.add(map);
        }

        return list;
    }*/

    /**
     * 设置可预约数量
     * @param orderSetting
     */
    @Override
    public void updateNumberByOrderDate(OrderSetting orderSetting) {

        //通过日期去数据库查询，判断是新增还是修改
        //通过日期到数据库查询有没有保存过
        Integer count = orderSettingMapper.findByOrderDate(orderSetting.getOrderDate());
        /**
         * 通过日期查询OrderSetting对象，判断如果是null，就执行新增，如果不是null，就执行修改
         *
         * 修改之前判断传递过来的修改的数值和查询出来的已预约数量，如果大于已预约数量，抛出异常，提示用户。
         *
         *
         */

        if (count>0){
            //如果保存过，那么就执行修改
            orderSettingMapper.updateNumberByOrderDate(orderSetting);
        }else{
            //如果没有保存过，那么就执行新增
            orderSettingMapper.add(orderSetting);
        }


    }
}
