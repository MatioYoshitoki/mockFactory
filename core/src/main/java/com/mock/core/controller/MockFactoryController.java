package com.mock.core.controller;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.util.ReturnUtil;
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
    public JsonPublic getManifest(@PathVariable("token") String token) throws ExceptionPlus {
        return ReturnUtil.makeSuccessReturn(mockService.getManifestList(token));
    }

    @GetMapping(value = "getInterfaceList/{token}/{manifestID}")
    public JsonPublic getInterfaceList(@PathVariable("manifestID") String manifestID, @PathVariable("token") String token) throws ExceptionPlus {
        return ReturnUtil.makeSuccessReturn(mockService.getInterfaceList(manifestID, token));
    }

    @GetMapping(value = "getManifestBase/{token}/{manifestID}")
    public JsonPublic getManifestBase(@PathVariable("manifestID") String manifestID, @PathVariable("token") String token) throws ExceptionPlus {
        return ReturnUtil.makeSuccessReturn(mockService.getManifestBase(manifestID, token));
    }

    @GetMapping(value = "getInterfaceDetail/{token}/{manifestID}/{interfaceID}")
    public JsonPublic getInterfaceDetail(@PathVariable("manifestID") String manifestID,@PathVariable("interfaceID") String interfaceID, @PathVariable("token") String token) throws ExceptionPlus {
        return ReturnUtil.makeSuccessReturn(mockService.getInterfaceDetail(interfaceID, manifestID, token));
    }

}
