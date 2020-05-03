package com.mock.common.exception;

import cn.hutool.core.util.StrUtil;

import java.util.List;

public class Assert {

    public static void isTrueDefault(boolean isTrue) throws ExceptionPlus {
        if (isTrue){
            throw new ExceptionPlus();
        }
    }

    public static void isTrue(boolean isTrue, int code, String msg) throws ExceptionPlus {
        if (isTrue){
            throw new ExceptionPlus(code, msg);
        }
    }

    public static void isNull(Object object, int code, String msg) throws ExceptionPlus {
        if (null==object){
            throw new ExceptionPlus(code, msg);
        }
    }

    public static void notNull(Object object, int code, String msg) throws ExceptionPlus {
        if (null!=object){
            throw new ExceptionPlus(code, msg);
        }
    }

    public static void isBlank(String string, int code, String msg) throws ExceptionPlus {
        if (StrUtil.isEmpty(string)){
            throw new ExceptionPlus(code, msg);
        }
    }

    public static void notBlank(String string, int code, String msg) throws ExceptionPlus {
        if (!StrUtil.isEmpty(string)){
            throw new ExceptionPlus(code, msg);
        }
    }

    public static void isEmpty(List<?> list, int code, String msg) throws ExceptionPlus {
        if (null == list){
            throw new ExceptionPlus(code, msg);
        }
        if (list.size()==0){
            throw new ExceptionPlus(code, msg);
        }
    }

}
