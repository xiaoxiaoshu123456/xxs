package com.itheima.mapper;

import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealMapper {
    void add(Setmeal setmeal);

    /**
     * 只要参数不是pojo或者map,不管是一个参数还是多个参数都加上@Param
     *
     * @param checkGroupIds
     * @param id
     */
    void setSetmealAndCheckGroup(@Param("checkGroupIds")Integer[] checkGroupIds, @Param("id")Integer id);

    List<Setmeal> findByCondition(@Param("queryString")String queryString);

    List<Setmeal> findSetmealAll();

    Setmeal findById(@Param("id")Integer id);

    List<Map> getSetmealReport();
}
