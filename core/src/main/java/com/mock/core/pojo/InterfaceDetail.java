package com.mock.core.pojo;

import lombok.*;

import java.util.List;

/**
 * Created by matioyoshitoki on 2020/2/7.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterfaceDetail {

    private List<InterfaceParamsPo> interfaceParamsList;
    private List<NullReturnPo> returnList;


}
