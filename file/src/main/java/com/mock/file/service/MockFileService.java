package com.mock.file.service;

import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.MockFilePo;
import com.mock.file.pojo.SingleInterfacePo;

import java.util.List;

public interface MockFileService {

    String getMock(List<SingleInterfacePo> interfaceList, String manifestID, String port) throws Exception;

    JsonPublic getMockByFile(MockFilePo mockFilePo, String token) throws Exception;

}
