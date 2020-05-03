package com.mock.customer.service;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserLoginInfo;
import com.mock.common.pojo.UserPo;
import com.mock.common.pojo.UserRegisterPo;

public interface UserService {

    UserPo login(UserLoginInfo userLoginInfo) throws ExceptionPlus;

    UserPo register(UserRegisterPo userRegister) throws ExceptionPlus;

//    UserPo tokenCheck(String token) throws ExceptionPlus;
}
