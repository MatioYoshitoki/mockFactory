package com.mock.core.pojo;

import lombok.*;

/**
 * Created by matioyoshitoki on 2020/1/13.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnContentPo {

    private String returnContentID;
    private String interfaceID;
    private String mockContent;
    private String viewContent;


}
