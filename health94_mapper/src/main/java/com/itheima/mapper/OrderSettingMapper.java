package com.itheima.mapper;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingMapper {
    Integer findByOrderDate(@Param("orderDate")Date orderDate);

    void updateNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<Map> findOrderSettingByMonth(@Param("dateBegin")String dateBegin, @Param("dateEnd")String dateEnd);

    OrderSetting findOrderSettingByOrderDate(@Param("date")Date date);

    void updateReservationsByOrderDate(@Param("date")Date date);

    //List<OrderSetting> findOrderSettingByMonth(@Param("dateBegin")String dateBegin,@Param("dateEnd") String dateEnd);
    
}
