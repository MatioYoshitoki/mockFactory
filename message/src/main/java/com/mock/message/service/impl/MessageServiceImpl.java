package com.mock.message.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mock.common.global.CloudCode;
import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.util.MyStrUtil;
import com.mock.message.configuration.MessageProperties;
import com.mock.message.dao.UserMapper;
import com.mock.message.service.MessageService;
import com.mock.message.util.AliSendMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * Created by matioyoshitoki on 2020/2/9.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    UserMapper userMapper;
    @Resource
    JedisPool jedisPool;
    @Resource
    MessageProperties messageProperties;

    @Override
    public JsonPublic sendMsg(String phoneNo) throws HTTPException, IOException {
        if (MyStrUtil.isEmail(phoneNo)){
            return new JsonPublic(CloudCode.NO_PHONE_NO, CloudCode.NO_PHONE_NO_MESSAGE, null);
        }

        String tmp = userMapper.checkPhoneNo(phoneNo);

        if (!StrUtil.isEmpty(tmp)){
            return new JsonPublic(CloudCode.USER_EXIST, CloudCode.USER_EXIST_MESSAGE, null);
        }

        String checkCode = ("" + ((Math.random() * 8999) + 1000)).split("\\.")[0];
        JSONObject templateParam = new JSONObject();
        templateParam.put("code", checkCode);

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(phoneNo,60*5, checkCode);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }

        return AliSendMS.aliSendMS(messageProperties.getTemplateCode(), phoneNo, templateParam.toJSONString(), messageProperties.getSignName());
    }

}
