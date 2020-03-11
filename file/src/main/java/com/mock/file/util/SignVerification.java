package com.mock.file.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mock.common.pojo.UserPo;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

// 登录验证
@Component
public class SignVerification {

    @Resource
    JedisPool jedisPool;

    public boolean verification() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")){
                Jedis jedis = null;
                try {
                    jedis = jedisPool.getResource();
                    String token = cookie.getValue();
                    if (!jedis.exists(token)){
                        return false;
                    }
                    String userStr = jedis.get(token);
                    if (StrUtil.isEmpty(userStr)){
                        return false;
                    }
                    UserPo userPo;
                    try {
                        userPo = JSON.parseObject(userStr, UserPo.class);
                    }catch (Exception e){
                        return false;
                    }
                    request.setAttribute("userPo", userPo);
                    break;
                }finally {
                    if (jedis!=null){
                        jedis.close();
                    }
                }
            }
        }

        return true;
    }

}