package com.mock.api.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by matioyoshitoki on 2020/1/20.
 */

@RequestMapping(value = "/")
@Controller
public class PageController {

    @GetMapping("/homePage")
    public String homePage(){
        return "homePage"; //当浏览器输入/homePage，会返回 /html/homePage.html页面
    }


    @GetMapping("/index")
    public String index(){
        return "index"; //当浏览器输入/index时，会返回 /html/index.html页面
    }

    @GetMapping("/interfaceList")
    public String interfaceList(){
        return "interface"; //当浏览器输入/index时，会返回 /html/index.html页面
    }

    @GetMapping("/detail")
    public String interfaceDetail(){
        return "interfaceDetail"; //当浏览器输入/index时，会返回 /html/index.html页面
    }

}
