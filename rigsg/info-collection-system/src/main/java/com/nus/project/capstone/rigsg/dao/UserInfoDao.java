package com.nus.project.capstone.rigsg.dao;

import com.nus.project.capstone.rigsg.dto.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/userinfo")
public class UserInfoDao {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoDao.class);
    public static final String rootMapperPath = "com.nus.project.capstone.rigsg.mapper.UserInfoMapper";
    public static final String selectAllQuery = ".getAllUser";

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    @GetMapping("/test")
    public String getTestMessage(){
        return "Hello World @ UserInfoDao";
    }

    @GetMapping("/getAllUser")
    public List<User> getAllUsers() {
        return sqlSessionTemplate.selectList(rootMapperPath + selectAllQuery);
    }

}
