package com.mock.api.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class CookieUtil {


    public static List<String> getCookieList(HttpServletRequest request) {
        List<String> cookieList = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return cookieList;
        }

        for (Cookie cookie : cookies) {
            if (!"token".equals(cookie.getName())){
                continue;
            }
            cookieList.add(cookie.getName() + "=" + cookie.getValue());
        }

        return cookieList;
    }

}
