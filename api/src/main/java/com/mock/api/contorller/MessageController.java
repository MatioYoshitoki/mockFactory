package com.mock.api.contorller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.mock.api.myHandler.MessageBlockHandler;
import com.mock.api.myHandler.MessageFallbackHandler;
import com.mock.common.pojo.JsonPublic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created by matioyoshitoki on 2020/2/9.
 */
@RestController
@RequestMapping(value = "/message")
@Slf4j
@RefreshScope
public class MessageController {

    @Resource
    RestTemplate restTemplate;

    @Value("${service-url.gateway-service}")
    String messageServiceURL;
//
    @GetMapping(value = "send")
    @SentinelResource(
            value = "messageSend",
            blockHandlerClass = MessageBlockHandler.class,
            blockHandler = "messageSendException",
            fallbackClass = MessageFallbackHandler.class,
            fallback = "messageSendFallback"
    )
    public JsonPublic send(@RequestParam("phoneNo") String phoneNo) {
        log.info("消息服务请求地址:"+messageServiceURL);
        log.info("手机号:"+phoneNo);
        return restTemplate.getForObject(messageServiceURL+"/message/send/"+phoneNo, JsonPublic.class);
    }


}