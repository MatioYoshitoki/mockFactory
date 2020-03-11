package com.mock.file.dao;

import com.mock.file.pojo.InterfaceParamsPo;
import com.mock.file.pojo.InterfacePo;
import com.mock.file.pojo.ManifestPo;
import com.mock.file.pojo.ReturnContentPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by matioyoshitoki on 2020/1/25.
 */
@Mapper
public interface ManifestMapper {

    void addManifest(ManifestPo manifestPo);

    void addInterface(InterfacePo interfacePo);

    void addReturnContent(ReturnContentPo returnContentPo);

    void addInterfaceParams(InterfaceParamsPo interfaceParamsPo);

}
