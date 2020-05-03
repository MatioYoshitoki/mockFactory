package com.mock.message.util;

import com.mymq.client.client.produce.Produce;
import com.mymq.client.client.produce.ProduceImpl;
import com.mymq.client.factory.ClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProduceUtil {

    @Bean
    public Produce getProduce() throws Exception {
        return ClientFactory.getInstance().createProduce("127.0.0.1", 8833);
    }

}
