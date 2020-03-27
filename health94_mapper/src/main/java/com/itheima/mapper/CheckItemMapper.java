package com.itheima.mapper;

import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemMapper {
    void add(CheckItem checkItem);

    List<CheckItem> findByCondition(@Param("queryString")String queryString);

    void deleteById(@Param("id")Integer id);

    Integer findCountByCheckItemId(@Param("id")Integer id);

    CheckItem findById(@Param("id")Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAllCheckItem();

    List<Integer> findCheckItemIdsByCheckGroupId(@Param("id")Integer id);

    List<CheckItem> findCheckItemByCheckGroupIds(@Param("id")Integer id);

    //Long findCountByCondition(@Param("queryString")String queryString);

    //List<CheckItem> findByCondition(@Param("firstResult")int firstResult,@Param("pageSize") Integer pageSize,@Param("queryString")  String queryString);
}
