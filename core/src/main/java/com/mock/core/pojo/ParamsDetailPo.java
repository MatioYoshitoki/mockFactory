package com.mock.core.pojo;

import lombok.*;

import java.util.List;

/**
 * Created by matioyoshitoki on 2020/1/12.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParamsDetailPo {

    private String param;
    private List<ParamsDetailPo> listParam;
    private String paramName;
    private String paramType;//String, int, List, Map
    private boolean checkNull;
    private String nullReturn;

}
