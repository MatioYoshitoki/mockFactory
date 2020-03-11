package com.mock.file.pojo;

import lombok.*;

/**
 * Created by matioyoshitoki on 2020/1/15.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingleInterfacePo {

    String interfaceName;
    String interfaceType;
    String requestURL;
    String params;
    String returnContent;
}
