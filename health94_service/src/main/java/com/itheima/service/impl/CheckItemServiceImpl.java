package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.mapper.CheckItemMapper;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemMapper;

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {

        checkItemMapper.add(checkItem);

    }

    /**
     * 检查项的分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    /*@Override
    public PageResult findAll(Integer currentPage, Integer pageSize, String queryString) {

        //定义分页查询的起始值，即limit x , y 中的x值
        int firstResult = (currentPage-1)*pageSize;

        //条件查询总记录数
        Long total = checkItemMapper.findCountByCondition(queryString);

        //条件查询分页数据
        List<CheckItem> checkItemList = checkItemMapper.findByCondition(firstResult,pageSize,queryString);

        //封装pageResult
        PageResult pageResult = new PageResult(total, checkItemList);

        return pageResult;
    }*/

    @Override
    public PageResult findAll(Integer currentPage, Integer pageSize, String queryString) {

        //定义当前页的页面和页面大小
        PageHelper.startPage(currentPage,pageSize);

        //条件查询数据
        List<CheckItem> checkItemList = checkItemMapper.findByCondition(queryString);

        //将查询出来的数据进行包装
        PageInfo pageInfo = new PageInfo<CheckItem>(checkItemList);

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 通过id删除检查项
     * @param id
     */
    @Override
    public void deleteById(Integer id) {

        //判断当前检查项有没有被检查组引用
        Integer count = checkItemMapper.findCountByCheckItemId(id);
        if (count>0){
            //如果被引用过，抛出异常提示不能删除的原因
            throw new RuntimeException(MessageConstant.CHECKITEM_IS_QUOTED);
        }

        checkItemMapper.deleteById(id);

    }

    /**
     * 通过id查询检查项
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemMapper.findById(id);
    }

    /**
     * 编辑更新
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemMapper.edit(checkItem);
    }

    /**
     * 查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAllCheckItem() {
        return checkItemMapper.findAllCheckItem();
    }

    /**
     * 通过检查组id查询检查项id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkItemMapper.findCheckItemIdsByCheckGroupId(id);
    }
}
