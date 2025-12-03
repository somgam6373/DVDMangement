package com.example.dvdmangement;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/rentMovie"); // 👉 /rent API만 JWT 필요
        // 나중에 보호하고 싶은 API 있으면 .addPathPatterns("/rent", "/something") 식으로 추가
    }
}
