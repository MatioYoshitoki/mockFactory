package com.mock.gateway.filter;


import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class LogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        MultiValueMap<String, String> queryParam = exchange.getRequest().getQueryParams();
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        String statusCode = exchange.getResponse().getStatusCode().toString();

        log.info("请求地址:"+path);
        log.info("请求时间:"+ DateUtil.now());
        log.info("请求参数:");
        Iterator<Map.Entry<String, List<String>>> iterator = queryParam.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<String>> tmp = iterator.next();
            log.info("\t参数名:"+tmp.getKey());
            log.info("\t参数  :"+tmp.getValue());
        }
        log.info("请求结果:" + statusCode);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
