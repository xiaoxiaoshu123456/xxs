package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void importOrderSettings(List<OrderSetting> orderSettings);

    List<Map> findOrderSettingByMonth(String date);

    void updateNumberByOrderDate(OrderSetting orderSetting);
}
