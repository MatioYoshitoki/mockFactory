package com.mock.api.myHandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.mock.common.global.CloudCode;
import com.mock.common.pojo.JsonPublic;

public class MessageBlockHandler {

    public static JsonPublic messageSendException(String phoneNo, BlockException e){
        return new JsonPublic(CloudCode.SEND_2_MUCH, CloudCode.SEND_2_MUCH_MESSAGE, null);
    }
}
