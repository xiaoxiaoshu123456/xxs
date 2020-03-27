package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.mapper.MemberMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.OrderSettingMapper;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     *1、判断预约的日期有没有提供体检服务
     *
     *2、如果预约的日期提供了体检服务，还得判断已预约人数是否已满
     *
     *3、通过手机号码判断预约的人是否是会员
     *
     *       不是会员：将其注册为会员
     *
     *       是会员：通过会员编号、订单编号、预约日期判断是否重复预约了体检套餐
     *4、新增体检预约的订单
     *
     *5、订单下单成功之后，需要更新已预约数量
     *
     *
     * @param map
     * @return
     */

    @Override
    public Order add(Map map) throws Exception {

        //获取用户的手机号码
        String telephone = (String) map.get("telephone");
        //获取用户指定的提交日期
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);

        //通过日期查询预约设置
        OrderSetting orderSetting = orderSettingMapper.findOrderSettingByOrderDate(date);
        //1、判断预约的日期有没有提供体检服务
        if (orderSetting==null){
            //提示错误信息
            throw new RuntimeException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、如果预约的日期提供了体检服务，还得判断已预约人数是否已满
        if (orderSetting.getReservations()>=orderSetting.getNumber()){
            //提示用户预约已满
            throw new RuntimeException(MessageConstant.ORDER_FULL);
        }
        //3、通过手机号码判断预约的人是否是会员
        Member member = memberMapper.findByTelephone(telephone);
        //判断是否是会员
        if (member==null){
            //如果不是会员，将其注册为会员
            member = new Member();
            member.setName((String)map.get("name"));
            member.setIdCard((String)map.get("idCard"));
            member.setSex((String)map.get("sex"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            //执行注册会员
            memberMapper.add(member);
        }else{

            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
            //如果是会员，通过会员id、预约日期、套餐id查询订单，防止重复提交订单
            Order queryOrder = orderMapper.findByCondition(order);
            if (queryOrder!=null){
                //如果订单已存在，就抛出异常，提示用户不要重复预约
                throw new  RuntimeException(MessageConstant.HAS_ORDERED);
            }
        }
        //4、新增体检预约的订单
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
        order.setOrderDate(date);
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderMapper.add(order);

        //5、订单下单成功之后，需要更新已预约数量
        orderSettingMapper.updateReservationsByOrderDate(date);

        return order;
    }

    /**
     * 通过id查询订单详情
     */
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderMapper.findById(id);
        Date orderDate = (Date) map.get("orderDate");
        String date = DateUtils.parseDate2String(orderDate);
        map.put("orderDate",date);

        return map;
    }
}
