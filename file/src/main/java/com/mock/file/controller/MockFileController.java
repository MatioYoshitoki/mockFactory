package com.mock.file.controller;

import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.MockFilePo;
import com.mock.common.util.ReturnUtil;
import com.mock.file.service.MockFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("file")
@RefreshScope
public class MockFileController {

    @Resource
    MockFileService mockFileService;

    @PostMapping(value = "getByFile/{token}")
    public JsonPublic getMockByFile(
            @RequestBody MockFilePo mockFile,
            @PathVariable("token") String token
    ) throws Exception {
        return ReturnUtil.makeSuccessReturn(mockFileService.getMockByFile(mockFile, token));
    }


}
