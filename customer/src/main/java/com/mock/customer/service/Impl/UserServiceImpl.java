package com.mock.customer.service.Impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.mock.common.exception.ExceptionPlus;
import com.mock.common.global.CloudCode;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserLoginInfo;
import com.mock.common.pojo.UserPo;
import com.mock.common.pojo.UserRegisterPo;
import com.mock.common.util.MyStrUtil;
import com.mock.common.util.RedisUtil;
import com.mock.customer.dao.UserMapper;
import com.mock.customer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    JedisPool jedisPool;
    @Value("${default.avatar}")
    String defaultAvatar;

    @Override
    public JsonPublic login(UserLoginInfo userLoginInfo) throws ExceptionPlus {

        String userID = userLoginInfo.getUID();
        String password = userLoginInfo.getPwd();
        if (StrUtil.isEmpty(userID)){
            throw new ExceptionPlus(CloudCode.NO_USER_CODE, CloudCode.NO_USER_CODE_MESSAGE);
        }
        if (StrUtil.isEmpty(password)){
            throw new ExceptionPlus(CloudCode.NO_LOGIN_PASSWORDS, CloudCode.NO_LOGIN_PASSWORDS_MESSAGE);
        }
        UserPo user = null;
        password = SecureUtil.md5(password);
        if (MyStrUtil.isPhoneNo(userID)){
            user = userMapper.getUserByPhoneLogin(userID, password);
        }

        if (MyStrUtil.isEmail(userID)){
            user = userMapper.getUserByEmailLoin(userID, password);
        }

        if(null == user) {
            throw new ExceptionPlus(CloudCode.WRONG_PASSWORD, CloudCode.WRONG_PASSWORD_MESSAGE);
        }
        String token = SecureUtil.md5(RandomUtil.randomString(32));
        user.setToken(token);

        RedisUtil.pushToRedis(jedisPool, token, JSON.toJSONString(user));
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(user);
        return jsonPublic;
    }

    @Override
    @Transactional
    public JsonPublic register(UserRegisterPo userRegister) throws ExceptionPlus {

        String phoneNo = userRegister.getPhoneNo();
        String checkCode = userRegister.getCheckCode();
        String showName = userRegister.getShowName();
        String password = userRegister.getPwd();
        String passwordRepeat = userRegister.getPwdRep();

        String userID ;

        if (StrUtil.isEmpty(phoneNo)){
            throw new ExceptionPlus(CloudCode.NO_USER_CODE, CloudCode.NO_USER_CODE_MESSAGE);
        }
        if (StrUtil.isEmpty(checkCode)){
            throw new ExceptionPlus(CloudCode.NO_CHECK_CODE, CloudCode.NO_CHECK_CODE_MESSAGE);
        }
        if (StrUtil.isEmpty(password)){
            throw new ExceptionPlus(CloudCode.NO_LOGIN_PASSWORDS, CloudCode.NO_LOGIN_PASSWORDS_MESSAGE);
        }
        if (!password.equals(passwordRepeat)){
            throw new ExceptionPlus(CloudCode.DIF_PASSWORD, CloudCode.DIF_PASSWORD_MESSAGE);
        }

        try (Jedis jedis = jedisPool.getResource()) {
            if (!checkCode.equals(jedis.get(phoneNo))) {
                throw new ExceptionPlus(CloudCode.WRONG_CHECK_CODE, CloudCode.WRONG_CHECK_CODE_MESSAGE);
            }
            String tmp = userMapper.checkPhoneNo(phoneNo);

            if (!StrUtil.isEmpty(tmp)) {
                throw new ExceptionPlus(CloudCode.USER_EXIST, CloudCode.USER_EXIST_MESSAGE);
            }
            password = SecureUtil.md5(password);
            userID = SecureUtil.md5(phoneNo);
            userMapper.addUser(userID, showName, phoneNo);
            userMapper.addPhoneUserLoginInfo(userID, phoneNo, password);
        }

        String token = SecureUtil.md5(RandomUtil.randomString(32));
        UserPo user = new UserPo(userID, showName, defaultAvatar, token);

        RedisUtil.pushToRedis(jedisPool, token, JSON.toJSONString(user));
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(user);
        return jsonPublic;
    }

    @Override
    public JsonPublic tokenCheck(String token) {
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(RedisUtil.getFromRedis(jedisPool ,token, UserPo.class));
        return jsonPublic;
    }

}
