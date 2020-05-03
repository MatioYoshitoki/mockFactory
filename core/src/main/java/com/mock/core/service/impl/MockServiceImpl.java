package com.mock.core.service.impl;

import com.mock.common.exception.Assert;
import com.mock.common.exception.ExceptionPlus;
import com.mock.common.global.CacheNames;
import com.mock.common.global.CloudCode;
import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.UserPo;
import com.mock.core.dao.ManifestMapper;
import com.mock.core.pojo.*;
import com.mock.core.service.MockService;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by matioyoshitoki on 2020/1/12.
 */
@Service
public class MockServiceImpl implements MockService {

    @Resource
    ManifestMapper manifestMapper;
    @Resource
    RedisCacheManager redisCacheManager;


    @Override
    public List<ManifestSummaryPo> getManifestList(String token) throws ExceptionPlus {
        UserPo userPo = Objects.requireNonNull(redisCacheManager.getCache(CacheNames.USER_CACHE_NAME)).get(token, UserPo.class);
        Assert.isNull(userPo, CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
        return manifestMapper.getManifestList(userPo.getUserID(), null);
    }

    @Override
    public List<InterfacePo> getInterfaceList(String manifestID, String token) throws ExceptionPlus {
        UserPo userPo = Objects.requireNonNull(redisCacheManager.getCache(CacheNames.USER_CACHE_NAME)).get(token, UserPo.class);
        Assert.isNull(userPo, CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
        Assert.isTrue(manifestMapper.getManifestList(userPo.getUserID(), null).size()==0, CloudCode.NOT_YOUR_MANIFEST, CloudCode.NOT_YOUR_MANIFEST_MESSAGE);
        return manifestMapper.getInterfaceList(manifestID);
    }

    @Override
    public ManifestSummaryPo getManifestBase(String manifestID, String token) throws ExceptionPlus {
        UserPo userPo = Objects.requireNonNull(redisCacheManager.getCache(CacheNames.USER_CACHE_NAME)).get(token, UserPo.class);
        Assert.isNull(userPo, CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);


        List<ManifestSummaryPo> manifestList = manifestMapper.getManifestList(userPo.getUserID(), manifestID);
        Assert.isEmpty(manifestList, CloudCode.NOT_YOUR_MANIFEST, CloudCode.NOT_YOUR_MANIFEST_MESSAGE);

        return manifestList.get(0);
    }

    @Override
    public InterfaceDetail getInterfaceDetail(String interfaceID, String manifestID, String token) throws ExceptionPlus {

        UserPo userPo = Objects.requireNonNull(redisCacheManager.getCache(CacheNames.USER_CACHE_NAME)).get(token, UserPo.class);
        Assert.isNull(userPo, CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);

        List<ManifestSummaryPo> manifestList = manifestMapper.getManifestList(userPo.getUserID(), null);
        Assert.isEmpty(manifestList, CloudCode.NOT_YOUR_MANIFEST, CloudCode.NOT_YOUR_MANIFEST_MESSAGE);

        List<InterfaceParamsPo> interfaceParamsList = manifestMapper.getInterfaceParams(interfaceID);

        String successReturn = manifestMapper.getInterfaceReturnContent(interfaceID);

        List<NullReturnPo> nullReturnList = new ArrayList<>();

        nullReturnList.add(new NullReturnPo("成功", successReturn));
        interfaceParamsList.forEach(one->{
            if (CloudGlobal.YES.equals(one.getCheckNull())){
                nullReturnList.add(new NullReturnPo("当'"+one.getParamName()+"'参数为空时", one.getNullReturn()));
            }
            switch (one.getParamType()){
                case "int":
                    one.setParamType("int");
                    break;
                case "float":
                    one.setParamType("float");
                    break;
                case "string":
                case "cname":
                case "md5":
                default:
                    one.setParamType("String");
                    break;
            }
            one.setNullReturn(null);
        });

        return new InterfaceDetail(interfaceParamsList, nullReturnList);
    }

}
