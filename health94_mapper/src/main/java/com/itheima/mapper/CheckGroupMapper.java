package com.itheima.mapper;

import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupMapper {
    void add(CheckGroup checkGroup);

    /**
     * param在一个参数的时候可以不写，前提是没有使用到动态sql,如果使用的动态sql，即使是一个参数也要写@Param
     *
     *
     * 只要参数不是pojo和map,不管是一个参数还是多个参数都加上@Param
     *
     * @param checkItemIds
     * @param id
     */
    void setCheckGroupAndCheckItem(@Param("checkItemIds")Integer[] checkItemIds,@Param("id") Integer id);

    List<CheckGroup> findByCondition(@Param("queryString")String queryString);

    void deleteAssocation(@Param("id")Integer id);

    void edit(CheckGroup checkGroup);

    Integer findCountById(@Param("id")Integer id);

    void deleteById(@Param("id")Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupBySetmealId(@Param("id")Integer id);

    //void setCheckGroupAndCheckItem(@Param("checkItemId")Integer checkItemId,@Param("id") Integer id);

}
