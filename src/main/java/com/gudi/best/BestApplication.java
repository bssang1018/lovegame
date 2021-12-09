package com.gudi.best;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@EnableScheduling
@SpringBootApplication
public class BestApplication {
    // @EnableScheduling 이거는 스케줄러 사용 사용할 클래스에 @Component
    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:aws.properties,classpath:application.properties";

    public static void main(String[] args) {

        new SpringApplicationBuilder(BestApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
