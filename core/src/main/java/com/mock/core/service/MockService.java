package com.mock.core.service;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;

public interface MockService {

    JsonPublic getManifestList(String token) throws ExceptionPlus;

    JsonPublic getInterfaceList(String manifestID ,String token) throws ExceptionPlus;

    JsonPublic getManifestBase(String manifestID, String token) throws ExceptionPlus;

    JsonPublic getInterfaceDetail(String interfaceID, String manifestID, String token) throws ExceptionPlus;

}
