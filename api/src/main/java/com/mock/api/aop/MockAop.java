package com.mock.api.aop;

import com.mock.api.util.SignVerification;
import com.mock.common.global.CloudCode;
import com.mock.common.pojo.JsonPublic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Aspect
public class MockAop {

    @Resource
    SignVerification signVerification;


    String source = "MockFactoryCloud";//代表项目编号，一个项目组都要有一个统一的编号。系统将这个字符串转为首字母大写，其他小写的形式作为项目的标识。


    // 定义切入点
    @Pointcut("@annotation(com.mock.api.annotion.Permission)")
    public void checkAccess(){

    }

    @Around("checkAccess()")
    public Object signVerification(ProceedingJoinPoint pjp) throws Throwable{

        if (signVerification.verification()){

            return pjp.proceed();//继续执行被拦截的方法
        }
        else {
            return new JsonPublic(CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE, null);
        }
    }

}