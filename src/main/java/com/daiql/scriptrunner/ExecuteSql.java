package com.daiql.scriptrunner;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author daiql
 * @description ScriptRunner run sql file
 * @date 2019/11/19 13:44
 */
@Service
public class ExecuteSql {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteSql.class);

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;


    public void executeSqlFile(String sqlFileName) throws Exception {

        if (StringUtils.isEmpty(sqlFileName)) {
            return;
        }

        Exception error = null;
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName, password);
            conn.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setAutoCommit(false);
            runner.setStopOnError(true);
            runner.setSendFullScript(false);
            runner.setDelimiter(";");
            runner.setFullLineDelimiter(false);
            LOGGER.info("read sql file");
            Resource resource = new ClassPathResource(sqlFileName);
            File file = resource.getFile();
            LOGGER.info("run sql file");
            runner.runScript(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            conn.commit();
        } catch (Exception e) {
            LOGGER.info("run sql error, start roll back");
            conn.rollback();
            error = e;
        } finally {
            close(conn);
        }
        if (error != null) {
            throw error;
        }
    }


    private static void close(Connection conn) {
        LOGGER.info("close connection");
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            if (conn != null) {
                conn = null;
            }
        }
    }
}

