package com.mock.message.controller;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.util.ReturnUtil;
import com.mock.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("message")
public class MessageController {

    @Resource
    MessageService messageService;

    @GetMapping(value = "send/{phoneNo}")
    public JsonPublic send(@PathVariable String phoneNo) throws IOException, ExceptionPlus {
        messageService.sendMsg(phoneNo);
        return ReturnUtil.makeSuccessReturn();
    }

}
