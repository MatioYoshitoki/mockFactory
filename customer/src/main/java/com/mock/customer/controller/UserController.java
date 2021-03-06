package com.mock.customer.controller;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserLoginInfo;
import com.mock.common.pojo.UserRegisterPo;
import com.mock.common.util.ReturnUtil;
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
    public JsonPublic login(@RequestBody UserLoginInfo user) throws ExceptionPlus {
        return ReturnUtil.makeSuccessReturn(userService.login(user));
    }

    @PostMapping(value = "/register")
    public JsonPublic register(@RequestBody UserRegisterPo userRegister) throws ExceptionPlus {
        return ReturnUtil.makeSuccessReturn(userService.register(userRegister));
    }

}
