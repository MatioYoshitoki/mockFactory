package com.mock.message.runner;

import com.mock.common.global.CloudGlobal;
import com.mock.common.listener.SMSListener;
import com.mymq.client.factory.ClientFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsumerRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ClientFactory.getInstance().createConsumer(CloudGlobal.SMS_TOPIC, CloudGlobal.SMS_TAG, new SMSListener(), 8833);
    }
}
