package com.mock.core.service;

import com.mock.common.exception.ExceptionPlus;
import com.mock.core.pojo.InterfaceDetail;
import com.mock.core.pojo.InterfacePo;
import com.mock.core.pojo.ManifestSummaryPo;

import java.util.List;

public interface MockService {

    List<ManifestSummaryPo> getManifestList(String token) throws ExceptionPlus;

    List<InterfacePo> getInterfaceList(String manifestID , String token) throws ExceptionPlus;

    ManifestSummaryPo getManifestBase(String manifestID, String token) throws ExceptionPlus;

    InterfaceDetail getInterfaceDetail(String interfaceID, String manifestID, String token) throws ExceptionPlus;

}
