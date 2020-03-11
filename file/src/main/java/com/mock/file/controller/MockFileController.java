package com.mock.file.controller;

import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.MockFilePo;
import com.mock.file.annotion.Permission;
import com.mock.file.service.MockFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("file")
public class MockFileController {

    @Resource
    MockFileService mockFileService;

    @PostMapping(value = "getByFile/{token}")
    @Permission
    public JsonPublic getMockByFile(
            @RequestBody MockFilePo mockFile,
            @PathVariable("token") String token
    ) throws Exception {
        return mockFileService.getMockByFile(mockFile, token);
    }


}
