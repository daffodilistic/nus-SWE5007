package com.nus.project.capstone.rigsg.mapper;

import com.nus.project.capstone.rigsg.dto.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserInfoMapper {
    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "firstName", column = "firstname"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "email", column = "email"),
            @Result(property = "address", column = "address"),
            @Result(property = "country", column = "country"),
            @Result(property = "phoneNumber", column = "phonenumber"),
            @Result(property = "isQualified", column = "isqualified"),
            @Result(property = "userId", column = "userid"),
    })
    List<User> getAllUser();
}
