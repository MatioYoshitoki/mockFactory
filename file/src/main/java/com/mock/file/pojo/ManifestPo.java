package com.mock.file.pojo;

import lombok.*;

/**
 * Created by matioyoshitoki on 2020/2/5.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManifestPo {

    private String manifestID;
    private String manifestName;
    private String port;
    private String groupID;
    private String authorID;

}
