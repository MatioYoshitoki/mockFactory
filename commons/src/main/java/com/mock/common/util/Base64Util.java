package com.mock.common.util;





import cn.hutool.core.codec.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64Util {

    public static InputStream base64ToInputStream(String base64) {

        byte[] bytes = Base64.decode(base64);

        return new ByteArrayInputStream(bytes);
    }
}