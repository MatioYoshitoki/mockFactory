package com.mock.core.service;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;

public interface MockService {
//    String getMock(List<SingleInterfacePo> interfaceList, String manifestID, String port) throws Exception;
//
//    String getMockByFile(String fileBase64, String manifestName, String fileName, String port, HttpServletRequest request) throws Exception;

    JsonPublic getManifestList(String token) throws ExceptionPlus;

    JsonPublic getInterfaceList(String manifestID ,String token) throws ExceptionPlus;

    JsonPublic getManifestBase(String manifestID, String token) throws ExceptionPlus;

    JsonPublic getInterfaceDetail(String interfaceID, String manifestID, String token) throws ExceptionPlus;

}
