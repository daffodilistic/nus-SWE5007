package com.nus.project.capstone.game;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameArenaApplicationTest {

    @Test
    void verifyPwdIsDummyFromApplicationYaml() {
        try (InputStream inputStream = getClass().getResourceAsStream("/" + "application.yml")) {
            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(inputStream);
            Map<String, Object> springData = (Map<String, Object>) yamlData.get("spring");
            Map<String, Object> dbData = (Map<String, Object>) springData.get("datasource");
            String pwd = (String) dbData.get("password");
            assertEquals("password", pwd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
