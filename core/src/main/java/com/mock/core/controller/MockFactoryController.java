package com.mock.core.controller;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.core.annotion.Permission;
import com.mock.core.service.MockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("mock")
public class MockFactoryController {

    @Resource
    MockService mockService;

    @GetMapping(value = "getManifestList/{token}")
    @Permission
    public JsonPublic getManifest(@PathVariable("token") String token){
        try {
            return mockService.getManifestList(token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

    @GetMapping(value = "getInterfaceList/{token}/{manifestID}")
    @Permission
    public JsonPublic getInterfaceList(@PathVariable("manifestID") String manifestID, @PathVariable("token") String token){
        try {
            return mockService.getInterfaceList(manifestID, token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

    @GetMapping(value = "getManifestBase/{token}/{manifestID}")
    @Permission
    public JsonPublic getManifestBase(@PathVariable("manifestID") String manifestID, @PathVariable("token") String token){
        try {
            return mockService.getManifestBase(manifestID, token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

    @GetMapping(value = "getInterfaceDetail/{token}/{manifestID}/{interfaceID}")
    @Permission
    public JsonPublic getInterfaceDetail(@PathVariable("manifestID") String manifestID,@PathVariable("interfaceID") String interfaceID, @PathVariable("token") String token){
        try {
            return mockService.getInterfaceDetail(interfaceID, manifestID, token);
        } catch (ExceptionPlus exceptionPlus) {
            exceptionPlus.printStackTrace();
            return new JsonPublic(exceptionPlus);
        }
    }

}
