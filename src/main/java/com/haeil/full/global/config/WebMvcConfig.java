package com.haeil.full.global.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
        interceptor.setEntityManagerFactory(entityManagerFactory);
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 기본 OSIV 설정(open-in-view: true)은 모든 요청에 대해 세션을 유지하므로 SSE 연결 시 DB 커넥션을 점유하는 문제가 발생함.
        // 따라서 application.yml에서 open-in-view: false로 자동 설정을 끄고,
        // 여기서 수동으로 인터셉터를 등록하되 SSE 구독 요청만 제외(exclude)시킴.
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/notifications/subscribe");
    }
}

