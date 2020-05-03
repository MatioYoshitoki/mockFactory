package com.mock.api.util;

import com.mock.common.global.CacheNames;
import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.UserPo;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

// 登录验证
@Component
public class SignVerification {

    @Resource
    RedisCacheManager redisCacheManager;

    public boolean verification() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(CloudGlobal.TOKEN)){
                String token = cookie.getValue();
                UserPo userPo = Objects.requireNonNull(redisCacheManager.getCache(CacheNames.USER_CACHE_NAME)).get(token, UserPo.class);
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