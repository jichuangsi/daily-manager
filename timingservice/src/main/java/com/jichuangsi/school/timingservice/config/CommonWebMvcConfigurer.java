package com.jichuangsi.school.timingservice.config;

import com.jichuangsi.school.timingservice.commons.TokenCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonWebMvcConfigurer implements WebMvcConfigurer {

    @Bean
    public TokenCheckInterceptor tokenCheckInterceptor() {
        return new TokenCheckInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，连接以/admin为前缀的 url路径
        registry.addInterceptor(tokenCheckInterceptor()).addPathPatterns("/**/**");
    }
}
