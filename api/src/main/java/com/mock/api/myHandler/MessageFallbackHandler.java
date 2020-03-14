package com.mock.api.myHandler;

import com.mock.common.global.CloudCode;
import com.mock.common.pojo.JsonPublic;

public class MessageFallbackHandler {

    public static JsonPublic messageSendFallback(String phoneNo, Throwable e){
        return new JsonPublic(CloudCode.SYSTEM_EXCEPTION_CODE, CloudCode.SYSTEM_EXCEPTION_MESSAGE, e.getMessage());
    }

}
