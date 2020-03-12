package com.mock.api.contorller;


import com.mock.api.annotion.Permission;
import com.mock.api.service.RestService;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserLoginInfo;
import com.mock.common.pojo.UserRegisterPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by matioyoshitoki on 2020/1/31.
 */
@RestController
@RequestMapping(value = "/user")
@Slf4j
@RefreshScope
public class UserController {

    @Value("${service-url.gateway-service}")
    private String customerServiceURL ;

    @Resource
    RestTemplate restTemplate;

    @PostMapping(value = "/login")
    public JsonPublic login(UserLoginInfo user){
        return restTemplate.postForObject(customerServiceURL+"/user/login", user, JsonPublic.class);
    }

    @PostMapping(value = "/register")
    public JsonPublic register(UserRegisterPo userRegister){
        return restTemplate.postForObject(customerServiceURL+"/user/register", userRegister, JsonPublic.class);
    }
//
    @PostMapping(value = "/tokenCheck")
    @Permission
    public JsonPublic tokenCheck(){
        return new JsonPublic();
    }

}
