package com.mock.common.pojo;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterPo {

    private String phoneNo;
    private String checkCode;
    private String showName;
    private String pwd;
    private String pwdRep;

}
