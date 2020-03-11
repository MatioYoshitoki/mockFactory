package com.mock.core.pojo;

import lombok.*;

/**
 * Created by matioyoshitoki on 2020/2/5.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterfacePo {

    private String interfaceID;
    private String manifestID;
    private String interfaceName;
    private String interfaceType;
    private String requestMap;
    private String mockPath;
    private String createTime;
    private String updateTime;

}
