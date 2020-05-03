package com.mock.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.mock.common.global.CloudCode;
import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.JsonPublic;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AliSendMS {


    public static JsonPublic aliSendMS(String templateCode, String mobile, String message, String signName) {
        
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", CloudGlobal.ACCESS_KEY_ID, CloudGlobal.SECRET);
//        DefaultProfile.addEndpoint("cn-hangzhou", "Dysmsapi","cn-hangzhou");

        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        try {
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setTemplateParam(message);
            request.setPhoneNumbers(mobile);
            SendSmsResponse httpResponse = client.getAcsResponse(request);
            log.info("mobile: " + mobile + " code: " + httpResponse.getMessage() + " templateCode: " + templateCode);
            if ("OK".equals(httpResponse.getCode())){
                return new JsonPublic();
            } else {
                log.error(httpResponse.getMessage());
                return new JsonPublic(Integer.parseInt(httpResponse.getCode()), httpResponse.getMessage(), null);
            }

        } catch (ClientException e) {
            e.printStackTrace();
            log.error(e.getErrMsg());
            if (e.getErrCode().equals("InvalidRecNum.Malformed")) {
                log.error("手机号格式错误");
                return new JsonPublic(CloudCode.NOT_SUPPORT_PHONE_NO, CloudCode.NOT_SUPPORT_PHONE_NO_MESSAGE, null);
            } else if (e.getErrCode().equals("InvalidSendSms")) {
                log.error("哎呀，短信请求太频繁啦，请稍后再试");
                return new JsonPublic(CloudCode.SEND_2_MUCH, CloudCode.SEND_2_MUCH_MESSAGE, null);
            } else {
                log.error("系统错误");
                return new JsonPublic(e);
            }
        }
    }
}