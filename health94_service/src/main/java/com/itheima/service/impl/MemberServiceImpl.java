package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.MemberMapper;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberMapper.add(member);
    }

    @Override
    public Map getMemberReport() throws Exception {

        //获取日历对象的实例
        Calendar calendar = Calendar.getInstance();
        //将日期往前倒推12个月,2019-03-26,2019-04-26,2019-05-26
        calendar.add(Calendar.MONTH,-12);

        //定义一个集合封装过去12个月的年月
        List<String> months = new ArrayList<>();
        //定义一个集合封装每一个月的会员数量
        List<Integer> memberCounts = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {

            //获取当前日期
            Date time = calendar.getTime();
            //获取过去12个月的数据 2019-03
            String month = DateUtils.parseDate2String(time, "yyyy-MM");
            months.add(month);

            //定义查询每个月会员数量的日期的起始值
            String dateBegin = month + "-01";
            //定义查询每个月会员数量的日期的结束值
            String dateEnd = month + "-31";

            Integer memberCount = memberMapper.findMemberCountByMonth(dateBegin,dateEnd);

            memberCounts.add(memberCount);

            calendar.add(Calendar.MONTH,+1);

        }

        //将数据封装到map中
        Map<String, List> map = new HashMap<>();
        map.put("months",months);
        map.put("memberCounts",memberCounts);

        return map;
    }


}
