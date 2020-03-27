package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping("getUsername")
    public Result getUsername(Principal principal){

        try {
            String name = principal.getName();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,name);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }

    }
}
