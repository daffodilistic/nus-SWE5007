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
            @Result(property = "userId", column = "user_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "email", column = "email"),
            @Result(property = "country", column = "country"),
            @Result(property = "state", column = "state"),
            @Result(property = "dateOfBirth", column = "date_of_birth"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "schoolName", column = "school_name"),
            @Result(property = "yearOfExp", column = "years_of_exp"),
            @Result(property = "isQualified", column = "is_qualified"),
            @Result(property = "teamId", column = "team_id")
    })
    List<User> getAllUser();
}
