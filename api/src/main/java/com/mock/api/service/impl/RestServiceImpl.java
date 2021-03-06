package com.mock.api.service.impl;

import com.mock.api.service.RestService;
import com.mock.api.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class RestServiceImpl implements RestService {


    @Override
    public HttpEntity getHttpEntity(HttpServletRequest request, MultiValueMap<String, Object> map) {
        List<String> cookieList = CookieUtil.getCookieList(request);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put("Cookie", cookieList);
        return new HttpEntity<>(map, requestHeaders);
    }
}
