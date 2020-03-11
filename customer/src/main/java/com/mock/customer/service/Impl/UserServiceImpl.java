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
        pushUserToRedis(user, token);
        user.setToken(token);
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

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            if (!checkCode.equals(jedis.get(phoneNo))){
                throw new ExceptionPlus(CloudCode.WRONG_CHECK_CODE, CloudCode.WRONG_CHECK_CODE_MESSAGE);
            }
            String tmp = userMapper.checkPhoneNo(phoneNo);

            if (!StrUtil.isEmpty(tmp)){
                throw new ExceptionPlus(CloudCode.USER_EXIST, CloudCode.USER_EXIST_MESSAGE);
            }
            password = SecureUtil.md5(password);
            userID = SecureUtil.md5(phoneNo);
            userMapper.addUser(userID, showName, phoneNo);
            userMapper.addPhoneUserLoginInfo(userID, phoneNo, password);
        }finally {
            if (jedis!=null) {
                jedis.close();
            }
        }
        String token = SecureUtil.md5(RandomUtil.randomString(32));
        UserPo user = new UserPo(userID, showName, defaultAvatar, token);
        pushUserToRedis(user, token);
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(user);
        return jsonPublic;
    }

    @Override
    public JsonPublic tokenCheck(String token) throws ExceptionPlus {
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(getUserPoFromRedis(token));
        return jsonPublic;
    }

    private void pushUserToRedis(UserPo user, String token) throws ExceptionPlus {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String result = jedis.setex(token,60 * 60 * 7, JSON.toJSON(user).toString());
            if (!"OK".equals(result)) {
                throw new ExceptionPlus(CloudCode.SYSTEM_EXCEPTION_CODE, CloudCode.SYSTEM_EXCEPTION_MESSAGE);
            }
            result = jedis.set(user.getUserID(), token);
            if (!"OK".equals(result)) {
                throw new ExceptionPlus(CloudCode.SYSTEM_EXCEPTION_CODE, CloudCode.SYSTEM_EXCEPTION_MESSAGE);
            }
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    private UserPo getUserPoFromRedis(String token) throws ExceptionPlus {
        Jedis jedis = null;
        UserPo userPo;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(token)){
                throw new ExceptionPlus(CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
            }
            userPo = JSON.parseObject(jedis.get(token), UserPo.class);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return userPo;
    }

}
