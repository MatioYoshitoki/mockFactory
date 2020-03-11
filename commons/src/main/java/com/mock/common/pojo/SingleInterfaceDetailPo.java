package com.mock.common.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

import java.util.List;

/**
 * Created by matioyoshitoki on 2020/1/15.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingleInterfaceDetailPo {

    private String interfaceID;

    private List<ParamsDetailPo> paramsDetailPoList;

    private JSONObject returnJson;
    private String viewContent;

    private String url;

}
