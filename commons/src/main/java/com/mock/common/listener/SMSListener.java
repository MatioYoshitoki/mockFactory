package com.mock.common.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mock.common.global.CloudGlobal;
import com.mock.common.util.AliSendMS;
import com.mymq.client.listener.MessageListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SMSListener implements MessageListener {
    @Override
    public void consumer(String tag, String topic, String msg) {

        if (CloudGlobal.SMS_TOPIC.equals(topic) && CloudGlobal.SMS_TAG.equals(tag)){
            JSONObject jsonObject = JSON.parseObject(msg);
            log.info(jsonObject.toJSONString());
            AliSendMS.aliSendMS(
                    jsonObject.getString(CloudGlobal.FIELD_TEMPLATE_CODE),
                    jsonObject.getString(CloudGlobal.FIELD_PHONE_NO),
                    jsonObject.getString(CloudGlobal.FIELD_TEMPLATE_PARAM),
                    jsonObject.getString(CloudGlobal.FIELD_SIGN_NAME)
            );
        }

    }
}
