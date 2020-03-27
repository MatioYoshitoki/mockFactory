package com.mock.customer.controller;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserLoginInfo;
import com.mock.common.pojo.UserRegisterPo;
import com.mock.customer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("user")
@RefreshScope
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("login")
    public JsonPublic login(@RequestBody UserLoginInfo user){
        try {
            return userService.login(user);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            log.error(user.toString()+"登录失败!");
            return new JsonPublic(exceptionPlus);
        }
    }

    @PostMapping(value = "/register")
    public JsonPublic register(@RequestBody UserRegisterPo userRegister){
        try {
            return userService.register(userRegister);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus.getCode(), exceptionPlus.getMessage(), null);
        }
    }
//
//    @PostMapping(value = "/tokenCheck")
//    @Permission
//    public JsonPublic tokenCheck(@CookieValue("token") String token){
//        try {
//            return userService.tokenCheck(token);
//        } catch (ExceptionPlus exceptionPlus) {
//            exceptionPlus.printStackTrace();
//            return new JsonPublic(exceptionPlus.getCode(), exceptionPlus.getMessage(), null);
//        }
//    }

}
