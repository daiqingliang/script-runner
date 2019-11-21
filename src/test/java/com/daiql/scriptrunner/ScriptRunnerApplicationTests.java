package com.daiql.scriptrunner;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScriptRunnerApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptRunnerApplicationTests.class);

    @Autowired
    private ExecuteSql executeSql;

    @Test
    void contextLoads() {
        try {
            executeSql.executeSqlFile("/sql/myTest.sql");
            LOGGER.info("Run sql success!");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("Run sql failed!" + e);
        }
    }

}
