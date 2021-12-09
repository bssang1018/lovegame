package com.gudi.best.config;

import com.gudi.best.util.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    //인터셉터가 동작 해야 될 요청 주소 mapping 목록
    // private static final List<String> URL_PATTERNS = Arrays.asList("/*");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor에 인터셉터 클래스 객체를 넣자
        // addPathPatterns : 해당 메소드는 동작해야할 url패턴을 설정합니다.
        // excludePathPatterns: 해당 메소드는 적용한 인터셉터에서 제외할 url패턴을 설정합니다.
        registry.addInterceptor(new Interceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/member/*", "/", "/js/**", "/css/**", "/img/**", "/kakao/*");
    }
}
