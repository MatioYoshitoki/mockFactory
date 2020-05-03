package com.mock.common.util;

import com.mock.common.pojo.JsonPublic;

public class ReturnUtil {

    public static JsonPublic makeSuccessReturn(Object data){
        JsonPublic result = new JsonPublic();
        result.setData(data);
        return result;
    }

    public static JsonPublic makeSuccessReturn(){
        return new JsonPublic();
    }

}
