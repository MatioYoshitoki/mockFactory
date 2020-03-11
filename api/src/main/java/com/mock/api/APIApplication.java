package com.mock.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class APIApplication {

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class ,args);
    }
}
