package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.mapper.CheckGroupMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Override
    public void add(Integer[] checkItemIds, CheckGroup checkGroup) {

        //新增检查组
        checkGroupMapper.add(checkGroup);

        //在检查组和检查项中间表中插入数据
        setCheckGroupAndCheckItem(checkItemIds,checkGroup.getId());

    }

    private void setCheckGroupAndCheckItem(Integer[] checkItemIds, Integer id) {

        if (checkItemIds!=null && checkItemIds.length>0){
            checkGroupMapper.setCheckGroupAndCheckItem(checkItemIds,id);
        }

    }

    /*private void setCheckGroupAndCheckItem(Integer[] checkItemIds, Integer id) {

        if (checkItemIds!=null && checkItemIds.length>0){
            for (Integer checkItemId : checkItemIds) {
                checkGroupMapper.setCheckGroupAndCheckItem(checkItemId,id);
            }
        }

    }*/

    /**
     * 查询检查组分页数据
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //定义页面大小和当前页的页码
        PageHelper.startPage(currentPage,pageSize);

        //条件查询数据
        List<CheckGroup> checkGroupList = checkGroupMapper.findByCondition(queryString);

        //将数据进行包装
        PageInfo pageInfo = new PageInfo<CheckGroup>(checkGroupList);

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 编辑更新检查组
     * @param checkItemIds
     * @param checkGroup
     */
    @Override
    public void edit(Integer[] checkItemIds, CheckGroup checkGroup) {

        //通过检查组的id删除中间表记录
        checkGroupMapper.deleteAssocation(checkGroup.getId());
        //设置检查组和检查项的中间表数据
        setCheckGroupAndCheckItem(checkItemIds,checkGroup.getId());
        //更新检查组数据
        checkGroupMapper.edit(checkGroup);

    }

    /**
     * 通过id删除检查组
     * @param id
     */
    @Override
    public void deleteById(Integer id) {

        //查询当前检查组有没有被套餐引用
        Integer count = checkGroupMapper.findCountById(id);
        if (count>0){
            //如果被套餐引用了，就抛出异常，提示错误信息
            throw  new RuntimeException(MessageConstant.CHECKGROUP_IS_QUOTED);
        }

        //删除检查组和检查项的中间表数据
        checkGroupMapper.deleteAssocation(id);

        //删除检查组
        checkGroupMapper.deleteById(id);

    }

    /**
     * 查询所有检查组信息
     * @return
     */

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }
}
