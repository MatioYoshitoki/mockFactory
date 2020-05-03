package com.mock.common.pojo;

import com.alibaba.fastjson.JSON;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SMSPo implements Serializable {

    private String templateCode;
    private String signName;
    private String phoneNo;
    private String templateParam;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
