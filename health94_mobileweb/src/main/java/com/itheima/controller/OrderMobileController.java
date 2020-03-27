package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderMobileController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("add")
    public Result add(@RequestBody Map map){

        try {
            //获取用户提交的验证码
            String validateCode = (String) map.get("validateCode");
            //获取手机号码
            String telephone = (String) map.get("telephone");
            //设置key
            String key = telephone+ RedisMessageConstant.SENDTYPE_ORDER;
            //获取后台发送给用户的验证码(redis中保存的验证码)
            String code = jedisPool.getResource().get(key);
            //对用户提交的验证码进行验证
            if (code==null || !code.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }else{
                //验证通过，提交预约，下达订单
                Order order =orderService.add(map);

                //下达订单成功之后，发送短信通知用户下单成功
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, ValidateCodeUtils.generateValidateCode(4).toString());

                return new Result(true,MessageConstant.ADD_ORDER_SUCCESS,order);
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ORDER_FAIL);
        }

    }

    /**
     * 通过id查询订单详情
     */
    @RequestMapping("findById")
    public Result findById(@RequestParam("id")Integer id){

        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
