package com.mock.core.dao;


import com.mock.core.pojo.InterfaceParamsPo;
import com.mock.core.pojo.InterfacePo;
import com.mock.core.pojo.ManifestSummaryPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by matioyoshitoki on 2020/1/25.
 */
@Mapper
public interface ManifestMapper {


    List<ManifestSummaryPo> getManifestList(@Param("userID") String userID, @Param("manifestID") String manifestID);

    List<InterfacePo> getInterfaceList(@Param("manifestID") String manifestID);

    List<InterfaceParamsPo> getInterfaceParams(@Param("interfaceID") String interfaceID);

    String getInterfaceReturnContent(@Param("interfaceID") String interfaceID);

}
