package com.mock.message.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "message")
@Setter
@Getter
@Component
public class MessageProperties {

    private String templateCode;
    private String signName;

}
