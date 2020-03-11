package com.mock.api.contorller;


import com.mock.api.annotion.Permission;
import com.mock.api.service.RestService;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.MockFilePo;
import com.mock.common.util.PojoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by matioyoshitoki on 2020/1/12.
 */

@RestController
@RequestMapping(value = "/mock")
public class MockFactoryController {


    @Resource
    RestTemplate restTemplate;

    @Value("${service-url.gateway-service}")
    String coreServiceURL;

    @Value("${service-url.gateway-service}")
    String fileServiceURL;


    @PostMapping(value = "getByFile")
    @Permission
    public JsonPublic getMockByFile(MockFilePo mockFile, @CookieValue("token") String token) throws Exception {
        return restTemplate.postForObject(fileServiceURL+"/file/getByFile/"+token, mockFile, JsonPublic.class);
    }
//
    @GetMapping(value = "getManifestList")
    @Permission
    public JsonPublic getManifest( @CookieValue("token") String token){
        return restTemplate.getForObject(coreServiceURL+"/mock/getManifestList/"+token, JsonPublic.class);
    }

    @GetMapping(value = "getInterfaceList")
    @Permission
    public JsonPublic getInterfaceList(@RequestParam("manifestID") String manifestID, @CookieValue("token") String token){
        return restTemplate.getForObject(coreServiceURL+"/mock/getInterfaceList/"+token+"/"+manifestID, JsonPublic.class);
    }

    @GetMapping(value = "getManifestBase")
    @Permission
    public JsonPublic getManifestBase(@RequestParam("manifestID") String manifestID, @CookieValue("token") String token){
        return restTemplate.getForObject(coreServiceURL+"/mock/getManifestBase/"+token+"/"+manifestID, JsonPublic.class);
    }

    @GetMapping(value = "getInterfaceDetail")
    @Permission
    public JsonPublic getInterfaceDetail(@RequestParam("manifestID") String manifestID, @RequestParam("interfaceID") String interfaceID, @CookieValue("token") String token){
        return restTemplate.getForObject(coreServiceURL+"/mock/getInterfaceDetail/"+token+"/"+manifestID+"/"+interfaceID,JsonPublic.class);
    }

}
