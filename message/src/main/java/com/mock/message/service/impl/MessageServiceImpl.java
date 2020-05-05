package com.mock.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mock.common.exception.Assert;
import com.mock.common.exception.ExceptionPlus;
import com.mock.common.global.CacheNames;
import com.mock.common.global.CloudCode;
import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.SMSPo;
import com.mock.message.util.AliSendMS;
import com.mock.common.util.MyStrUtil;
import com.mock.message.configuration.MessageProperties;
import com.mock.message.dao.UserMapper;
import com.mock.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.http.HTTPException;

/**
 * Created by matioyoshitoki on 2020/2/9.
 */
@Service
@Configuration
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    UserMapper userMapper;
    @Resource
    MessageProperties messageProperties;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    AliSendMS aliSendMS;

    @Override
    @CachePut(cacheNames = CacheNames.SMS_CACAHE_NAME, key = "#phoneNo")
    public String sendMsg(String phoneNo) throws HTTPException, ExceptionPlus {
        log.info("code:"+messageProperties.getTemplateCode());
        Assert.isTrue(MyStrUtil.isEmail(phoneNo), CloudCode.NO_PHONE_NO, CloudCode.NO_PHONE_NO_MESSAGE);
        String tmp = userMapper.checkPhoneNo(phoneNo);
        Assert.notBlank(tmp, CloudCode.USER_EXIST, CloudCode.USER_EXIST_MESSAGE);

        String checkCode = ("" + ((Math.random() * 8999) + 1000)).split("\\.")[0];
        JSONObject templateParam = new JSONObject();
        templateParam.put(CloudGlobal.CODE, checkCode);
        SMSPo smsPo = new SMSPo(messageProperties.getTemplateCode(), messageProperties.getSignName(), phoneNo, templateParam.toJSONString());
        rabbitTemplate.convertAndSend(CloudGlobal.SMS_EXCHANGE, CloudGlobal.SMS_ROUTE_KEY, smsPo);

        return checkCode;
    }

    @RabbitListener(queues = CloudGlobal.SMS_EXCHANGE)
    public void mqListener(SMSPo message) throws ExceptionPlus {
        log.info(message.toString());
        aliSendMS.aliSendMS(
            message.getTemplateCode(),
            message.getPhoneNo(),
            message.getTemplateParam(),
            message.getSignName()
        );

    }

}
