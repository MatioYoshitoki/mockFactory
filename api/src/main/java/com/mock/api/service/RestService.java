package com.mock.api.service;

import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;

public interface RestService {

    HttpEntity getHttpEntity(HttpServletRequest request, MultiValueMap<String, Object> map);

}
