package com.whs.dev2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}

// DB 환경변수 값 확인용 컴포넌트 (테스트 후 삭제 권장)
@Component
class EnvCheck {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @PostConstruct
    public void printEnv() {
        System.out.println("==== DB ENV CHECK ====");
        System.out.println("spring.datasource.url = " + datasourceUrl);
        System.out.println("spring.datasource.username = " + datasourceUsername);
        System.out.println("spring.datasource.password = " + datasourcePassword);
        System.out.println("======================");
    }
}
