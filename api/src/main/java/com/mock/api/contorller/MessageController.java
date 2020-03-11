package com.mock.api.contorller;


import com.mock.common.pojo.JsonPublic;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by matioyoshitoki on 2020/2/9.
 */
@RestController
@RequestMapping(value = "/message")
@Slf4j
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
