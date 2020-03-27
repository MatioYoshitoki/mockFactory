package com.mock.core.controller;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.core.service.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("mock")
@RefreshScope
public class MockFactoryController {

    @Resource
    MockService mockService;

    @GetMapping(value = "getManifestList/{token}")
    public JsonPublic getManifest(@PathVariable("token") String token){
        try {
            return mockService.getManifestList(token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

    @GetMapping(value = "getInterfaceList/{token}/{manifestID}")
    public JsonPublic getInterfaceList(@PathVariable("manifestID") String manifestID, @PathVariable("token") String token){
        try {
            return mockService.getInterfaceList(manifestID, token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

    @GetMapping(value = "getManifestBase/{token}/{manifestID}")
    public JsonPublic getManifestBase(@PathVariable("manifestID") String manifestID, @PathVariable("token") String token){
        try {
            return mockService.getManifestBase(manifestID, token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

    @GetMapping(value = "getInterfaceDetail/{token}/{manifestID}/{interfaceID}")
    public JsonPublic getInterfaceDetail(@PathVariable("manifestID") String manifestID,@PathVariable("interfaceID") String interfaceID, @PathVariable("token") String token){
        try {
            return mockService.getInterfaceDetail(interfaceID, manifestID, token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

}
