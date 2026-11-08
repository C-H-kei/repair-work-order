package com.dormitory.repairbackend.config;

import com.dormitory.repairbackend.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")
                // 放行登录接口
                .excludePathPatterns("/api/auth/login")
                // 放行 Knife4j/Swagger 相关的所有静态资源和接口
                .excludePathPatterns(
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/favicon.ico"
                );
    }
}