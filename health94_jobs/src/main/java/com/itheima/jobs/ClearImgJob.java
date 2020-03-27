package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;
@Component
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){

        //将七牛云的set集合的key和数据库set集合的key存入到sdiff方法中，进行set集合的比较。
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String img : set) {

            //通过七牛云的工具类去删除七牛云的多余的图片
            QiniuUtils.deleteFileFromQiniu(img);

            //删除七牛云的set集合中的多余的图片
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,img);

        }


    }
}
