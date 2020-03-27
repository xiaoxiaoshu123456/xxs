package com.itheima.service;

import com.itheima.pojo.Order;

import java.util.Map;

public interface OrderService {
    Order add(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
