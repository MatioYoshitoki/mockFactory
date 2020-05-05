package com.mock.message.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.mock.common.exception.Assert;
import com.mock.common.exception.ExceptionPlus;
import com.mock.common.global.CloudCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AliSendMS {

    @Value("${message.accessKeyID}")
    String accessKeyID;
    @Value("${message.secret}")
    String secret;

    public void aliSendMS(String templateCode, String mobile, String message, String signName) throws ExceptionPlus {
        
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyID, secret);

        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        try {
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setTemplateParam(message);
            request.setPhoneNumbers(mobile);
            SendSmsResponse httpResponse = client.getAcsResponse(request);
            log.info("mobile: " + mobile + " code: " + httpResponse.getMessage() + " templateCode: " + templateCode);

        } catch (ClientException e) {
            e.printStackTrace();
            Assert.isTrue(e.getErrCode().equals("InvalidRecNum.Malformed"), CloudCode.NOT_SUPPORT_PHONE_NO, CloudCode.NOT_SUPPORT_PHONE_NO_MESSAGE);
            Assert.isTrue(e.getErrCode().equals("InvalidSendSms"), CloudCode.SEND_2_MUCH, CloudCode.SEND_2_MUCH_MESSAGE);
        }
    }
}
