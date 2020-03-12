package com.mock.api.contorller;


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
import java.io.IOException;

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
    public JsonPublic send(@RequestParam("phoneNo") String phoneNo) throws IOException {
        log.info("消息服务请求地址:"+messageServiceURL);
        log.info("手机号:"+phoneNo);
        return restTemplate.getForObject(messageServiceURL+"/message/send/"+phoneNo, JsonPublic.class);
    }

}
