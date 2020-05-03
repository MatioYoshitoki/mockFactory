package com.mock.common.util;

import com.mymq.commons.pojo.Content;
import com.mymq.commons.util.SnowFlakeUtil;

public class ContentFactory {

    public static Content createNormalContent(){
        Content content = new Content(0, 0);
        content.setMsgID(SnowFlakeUtil.next());
        return content;
    }

}
