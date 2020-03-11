package com.mock.common.pojo;

import com.alibaba.fastjson.JSON;
import lombok.*;

import java.io.Serializable;

/**
 * Created by matioyoshitoki on 2020/1/25.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPo implements Serializable {

    private String userID;
    private String showName;
    private String avatar;
    private String token;
}
