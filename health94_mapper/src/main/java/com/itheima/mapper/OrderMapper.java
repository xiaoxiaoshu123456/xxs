package com.itheima.mapper;

import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {
    Order findByCondition(Order order);

    void add(Order order);

    Map findById(@Param("id")Integer id);

    Integer findTodayOrderNumber(@Param("today")String today);

    Integer findTodayVisitsNumber(@Param("today")String today);


    Integer findOrderNumberAfterDate(@Param("orderDate")String orderDate);

    Integer findVisitsNumberAfterDate(@Param("orderDate")String orderDate);

    List<Map> findHotSetmeal();
}
