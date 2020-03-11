package com.mock.common.pojo;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MockFilePo {

    private String file;
    private String manifestName;
    private String fileName;
    private String port;

}
