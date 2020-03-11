package com.mock.file.configuration;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "redis")
@Component
public class JedisProperties {

    private int maxIdle;
    private long maxWaitMillis;
    private int maxTotal;
    private int minIdle;
    private String host;
    private int port;
    private int timeout;

}
