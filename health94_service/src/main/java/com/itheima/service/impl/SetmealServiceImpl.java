package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.mapper.CheckGroupMapper;
import com.itheima.mapper.CheckItemMapper;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Autowired
    private CheckItemMapper checkItemMapper;

    @Override
    public void add(Integer[] checkGroupIds, Setmeal setmeal) {

        //新增套餐，配置主键回显
        setmealMapper.add(setmeal);

        //在套餐和检查组的中间表插入数据
        setSetmealAndCheckGroup(checkGroupIds,setmeal.getId());

        //将保存到数据库中的图片保存一份到redis的set集合中
        if (setmeal.getImg()!=null){
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        }

    }

    private void setSetmealAndCheckGroup(Integer[] checkGroupIds, Integer id) {
        if (checkGroupIds!=null && checkGroupIds.length>0){
            setmealMapper.setSetmealAndCheckGroup(checkGroupIds,id);
        }
    }

    /**
     * 分页查询套餐信息
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //定义页面大小和当前页页码
        PageHelper.startPage(currentPage,pageSize);

        //条件查询套餐信息
        List<Setmeal> setmealList = setmealMapper.findByCondition(queryString);

        //将查询出来的数据进行包装
        PageInfo pageInfo = new PageInfo<Setmeal>(setmealList);

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 查询所有套餐列表
     * @return
     */
    @Override
    public List<Setmeal> findSetmealAll() {
        return setmealMapper.findSetmealAll();
    }

    /**
     * 通过套餐id查询套餐、套餐对应的检查组，以及检查组对应的检查项数据
     */
   /* @Override
    public Setmeal findById(Integer id) {

        return setmealMapper.findById(id);
    }*/

    @Override
    public Setmeal findById(Integer id) {

        //通过套餐id查询套餐
        Setmeal setmeal = setmealMapper.findById(id);
        //通过套餐id查询检查组信息
        List<CheckGroup> checkGroups = checkGroupMapper.findCheckGroupBySetmealId(setmeal.getId());
        //将检查组信息封装到套餐中
        setmeal.setCheckGroups(checkGroups);

        //循环遍历每一个检查组，查询对应的检查项信息
        for (CheckGroup checkGroup : checkGroups) {
            //通过检查组的id查询对应的检查项的数据
            List<CheckItem> checkItems = checkItemMapper.findCheckItemByCheckGroupIds(checkGroup.getId());
            //将检查项的数据封装到检查组中
            checkGroup.setCheckItems(checkItems);
        }

        return setmeal;
    }

    /**
     * 查询套餐占比统计
     * @return
     */
    @Override
    public Map getSetmealReport() {

        //查询出套餐占比数据
        List<Map> list = setmealMapper.getSetmealReport();

        //定义一个list封装套餐名称
        List<String> setmealNames = new ArrayList<>();

        //遍历集合，获取其中的每一个套餐的套餐名称
        for (Map map : list) {
            String name = (String) map.get("name");
            setmealNames.add(name);
        }

        //将两个集合封装到map中
        Map<String, List> map = new HashMap<>();
        map.put("setmealNames",setmealNames);
        map.put("setmealCounts",list);

        return map;
    }
}
