package com.mock.api.util;

import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.UserPo;
import com.mock.common.util.RedisUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
            if (cookie.getName().equals(CloudGlobal.TOKEN)){
                String token = cookie.getValue();
                UserPo userPo = RedisUtil.getFromRedis(jedisPool, token, UserPo.class);
                if (null == userPo){
                    return false;
                }
                request.setAttribute(CloudGlobal.USER_PO, userPo);
                break;
            }
        }
        return true;
    }

}