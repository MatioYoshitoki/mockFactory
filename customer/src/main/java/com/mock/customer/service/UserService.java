package com.mock.customer.service;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserLoginInfo;
import com.mock.common.pojo.UserPo;
import com.mock.common.pojo.UserRegisterPo;

public interface UserService {

    JsonPublic login(UserLoginInfo userLoginInfo) throws ExceptionPlus;

    JsonPublic register(UserRegisterPo userRegister) throws ExceptionPlus;

    JsonPublic tokenCheck(String token) throws ExceptionPlus;
}
