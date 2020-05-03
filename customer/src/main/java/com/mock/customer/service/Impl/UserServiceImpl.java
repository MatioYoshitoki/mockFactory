package com.mock.customer.service.Impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.mock.common.exception.Assert;
import com.mock.common.exception.ExceptionPlus;
import com.mock.common.global.CacheNames;
import com.mock.common.global.CloudCode;
import com.mock.common.pojo.UserLoginInfo;
import com.mock.common.pojo.UserPo;
import com.mock.common.pojo.UserRegisterPo;
import com.mock.common.util.MyStrUtil;
import com.mock.customer.dao.UserMapper;
import com.mock.customer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Value("${default.avatar}")
    String defaultAvatar;

    @Resource
    RedisCacheManager redisCacheManager;

    @Override
    @CachePut(cacheNames = CacheNames.USER_CACHE_NAME, key = "#result.token")
    public UserPo login(UserLoginInfo userLoginInfo) throws ExceptionPlus {

        String userID = userLoginInfo.getUID();
        String password = userLoginInfo.getPwd();

        Assert.isBlank(userID, CloudCode.NO_USER_CODE, CloudCode.NO_USER_CODE_MESSAGE);
        Assert.isBlank(password, CloudCode.NO_LOGIN_PASSWORDS, CloudCode.NO_LOGIN_PASSWORDS_MESSAGE);

        UserPo user = null;
        password = SecureUtil.md5(password);
        if (MyStrUtil.isPhoneNo(userID)){
            user = userMapper.getUserByPhoneLogin(userID, password);
        }
        if (MyStrUtil.isEmail(userID)){
            user = userMapper.getUserByEmailLoin(userID, password);
        }
        Assert.isNull(user, CloudCode.WRONG_PASSWORD, CloudCode.WRONG_PASSWORD_MESSAGE);
        String token = SecureUtil.md5(RandomUtil.randomString(32));
        user.setToken(token);
        return user;
    }

    @Override
    @Transactional
    @CachePut(cacheNames = CacheNames.USER_CACHE_NAME, key = "#result.token")
    public UserPo register(UserRegisterPo userRegister) throws ExceptionPlus {

        String phoneNo = userRegister.getPhoneNo();
        String checkCode = userRegister.getCheckCode();
        String showName = userRegister.getShowName();
        String password = userRegister.getPwd();
        String passwordRepeat = userRegister.getPwdRep();

        String userID ;

        Assert.isBlank(phoneNo, CloudCode.NO_USER_CODE, CloudCode.NO_USER_CODE_MESSAGE);
        Assert.isBlank(checkCode, CloudCode.NO_CHECK_CODE, CloudCode.NO_CHECK_CODE_MESSAGE);
        Assert.isBlank(password, CloudCode.NO_LOGIN_PASSWORDS, CloudCode.NO_LOGIN_PASSWORDS_MESSAGE);
        Assert.isTrue(!password.equals(passwordRepeat), CloudCode.DIF_PASSWORD, CloudCode.DIF_PASSWORD_MESSAGE);

        Cache.ValueWrapper cacheCheckCode = Objects.requireNonNull(redisCacheManager.getCache(CacheNames.SMS_CACAHE_NAME)).get(phoneNo);

        Assert.isNull(cacheCheckCode, CloudCode.WRONG_CHECK_CODE, CloudCode.WRONG_CHECK_CODE_MESSAGE);
        Assert.isTrue(!checkCode.equals(cacheCheckCode.toString()), CloudCode.WRONG_CHECK_CODE, CloudCode.WRONG_CHECK_CODE_MESSAGE);

        String tmp = userMapper.checkPhoneNo(phoneNo);

        Assert.notBlank(tmp, CloudCode.USER_EXIST, CloudCode.USER_EXIST_MESSAGE);

        password = SecureUtil.md5(password);
        userID = SecureUtil.md5(phoneNo);
        userMapper.addUser(userID, showName, phoneNo);
        userMapper.addPhoneUserLoginInfo(userID, phoneNo, password);
        String token = SecureUtil.md5(RandomUtil.randomString(32));
        return new UserPo(userID, showName, defaultAvatar, token);
    }

}
