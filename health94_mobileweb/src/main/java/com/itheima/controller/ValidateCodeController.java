package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交预约功能中的发送验证码
     * @param telephone
     * @return
     */
    @RequestMapping("send2telephone")
    public Result send2telephone(@RequestParam("telephone")String telephone){

        try {
            //生成一个随机的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //给手机发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);

            //设置一个唯一的key
            String key = telephone+ RedisMessageConstant.SENDTYPE_ORDER;

            //将验证码存入到redis中，并设置一个10分钟的有效时间
            jedisPool.getResource().setex(key,60*10,code);

            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }

    /**
     * 用户登陆的发送验证码
     *
     */
    @RequestMapping("send2Login")
    public Result send2Login(@RequestParam("telephone")String telephone){

        try {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //将验证码发送到手机
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);

            //生成一个key
            String key = telephone+RedisMessageConstant.SENDTYPE_LOGIN;

            //将验证码存入到redis中，并且设置一个10分钟的有效时间
            jedisPool.getResource().setex(key,60*10,code);

            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
