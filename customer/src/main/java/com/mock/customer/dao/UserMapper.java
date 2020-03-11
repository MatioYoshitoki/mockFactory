package com.mock.customer.dao;

import com.mock.common.pojo.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by matioyoshitoki on 2020/1/25.
 */
@Mapper
public interface UserMapper {

    UserPo getUserByPhoneLogin(@Param("phoneNo") String phoneNo, @Param("password") String password);

    UserPo getUserByEmailLoin(@Param("email") String email, @Param("password") String password);

    String checkPhoneNo(@Param("phoneNo") String phoneNo);

    void addUser(@Param("userID") String userID, @Param("showName") String showName, @Param("phoneNo") String phoneNo);

    void addPhoneUserLoginInfo(@Param("userID") String userID, @Param("phoneNo") String phoneNo, @Param("password") String password);

}
