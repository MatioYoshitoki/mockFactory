package com.mock.common.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PojoUtil {

    public static MultiValueMap<String, Object> pojo2Map(Object obj) throws Exception{
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method getMethod = pd.getReadMethod();
            Object o = getMethod.invoke(obj);
            map.add(field.getName(), o);
        }
        return map;
    }
}
