package com.nus.project.capstone.rigsg.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Configuration
public class MySqlConfig {

    private final Logger logger = LoggerFactory.getLogger(MySqlConfig.class);

    @Value("${db.projectId}")
    private String projectId;
    @Value("${db.region}")
    private String region;
    @Value("${db.instanceId}")
    private String dbInstanceId;
    @Value("${db.name}")
    private String dbName;
    @Value("${db.username}")
    private String dbUser;
    @Value("${db.pwdFilePath}")
    private String dbPwdFilePath;

    private String getInstanceName(){
        return projectId + ":" + region + ":" + dbInstanceId;
    }

    private String extractPwd(){
        try(BufferedReader br = new BufferedReader(new FileReader(dbPwdFilePath))){
            return br.readLine();
        } catch (IOException ie){
            logger.error("Error in reading sql pwd file at [{}]", dbPwdFilePath);
            return null;
        }
    }

    @Bean(name = "sqlDataSource")
    public DataSource getConnectionPool() {
        String instanceName = getInstanceName();
        String dbPwd = extractPwd();
        if(dbPwd == null){
            logger.error("Null in reading sql pwd file at [{}]", dbPwdFilePath);
            throw new RuntimeException("Null in reading sql pwd file");
        }
        logger.info("Creating SQL connection with params: [instanceName={}, userName={}, password={}]",
                instanceName,
                dbUser,
                dbPwd);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql:///%s", dbName));
        config.setUsername(dbUser);
        config.setPassword(dbPwd);
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", instanceName);
        return new HikariDataSource(config);
    }

}
