package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;


public interface SetmealService {

    void add(Integer[] checkGroupIds, Setmeal setmeal);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findSetmealAll();

    Setmeal findById(Integer id);

    Map getSetmealReport();
}
