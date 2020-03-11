package com.mock.core.dao;


import com.mock.core.pojo.InterfaceParamsPo;
import com.mock.core.pojo.InterfacePo;
import com.mock.core.pojo.ManifestSummaryPo;
import com.mock.core.pojo.ReturnContentPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by matioyoshitoki on 2020/1/25.
 */
@Mapper
public interface ManifestMapper {

//    void addManifest(ManifestPo manifestPo);
//
//    void addInterface(InterfacePo interfacePo);

    void addInterfaceParams(InterfaceParamsPo interfaceParamsPo);

    void addReturnContent(ReturnContentPo returnContentPo);

    List<ManifestSummaryPo> getManifestList(@Param("userID") String userID, @Param("manifestID") String manifestID);

    List<InterfacePo> getInterfaceList(@Param("manifestID") String manifestID);

    List<InterfaceParamsPo> getInterfaceParams(@Param("interfaceID") String interfaceID);

    String getInterfaceReturnContent(@Param("interfaceID") String interfaceID);

}
