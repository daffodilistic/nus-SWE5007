package com.nus.project.capstone.rigsg.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = "com.nus.project.capstone.rigsg.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
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
    @Primary
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

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory mysqlSqlSessionFactory(
            @Qualifier("sqlDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate mysqlSqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
