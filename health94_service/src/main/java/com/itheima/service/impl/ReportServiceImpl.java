package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.MemberMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.pojo.Member;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {


    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

   /**
    * 运营数据统计
    reportDate:null,//报告日期
    todayNewMember :0,//今日新增会员数
    totalMember :0,//总会员数
    thisWeekNewMember :0,//本周新增会员数
    thisMonthNewMember :0,//本月新增会员数
    todayOrderNumber :0,//今日预约数
    todayVisitsNumber :0,//今日到诊数
    thisWeekOrderNumber :0,//本周预约数
    thisWeekVisitsNumber :0,//本周到诊数
    thisMonthOrderNumber :0,//本月预约数
    thisMonthVisitsNumber :0,//本月到诊数
    hotSetmeal ://显示4个热门套餐
    */
    @Override
    public Map getBusinessReport() throws Exception {

        //今日日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //本周一日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //本月一号日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //todayNewMember :今日新增会员数
        Integer todayNewMember = memberMapper.findTodayNewMember(today);

        //totalMember :总会员数
        Integer totalMember = memberMapper.findTotalMember();

        //thisWeekNewMember:本周新增会员数
        Integer thisWeekNewMember = memberMapper.findMemberCountAfterOrderDate(thisWeekMonday);
        //thisMonthNewMember :本月新增会员数
        Integer thisMonthNewMember = memberMapper.findMemberCountAfterOrderDate(firstDay4ThisMonth);

        //todayOrderNumber :今日预约数
        Integer todayOrderNumber = orderMapper.findTodayOrderNumber(today);

        //todayVisitsNumber :今日到诊数
        Integer todayVisitsNumber = orderMapper.findTodayVisitsNumber(today);

        //thisWeekOrderNumber :本周预约数
        Integer thisWeekOrderNumber = orderMapper.findOrderNumberAfterDate(thisWeekMonday);

        //thisMonthOrderNumber :本月预约数
        Integer thisMonthOrderNumber = orderMapper.findOrderNumberAfterDate(firstDay4ThisMonth);

        //thisWeekVisitsNumber :本周到诊数
        Integer thisWeekVisitsNumber = orderMapper.findVisitsNumberAfterDate(thisWeekMonday);
        //thisMonthVisitsNumber :本月到诊数
        Integer thisMonthVisitsNumber = orderMapper.findVisitsNumberAfterDate(firstDay4ThisMonth);

        //查询热门套餐
        List<Map> hotSetmeal = orderMapper.findHotSetmeal();

        //将查询出来的数据全部封装到map
        Map<String,Object> result = new HashMap();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);

        return result;
    }
}
