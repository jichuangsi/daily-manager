package com.jichuangsi.school.timingservice.config;

import com.jichuangsi.school.timingservice.commons.CrosInterceptor;
import com.jichuangsi.school.timingservice.commons.TokenCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonWebMvcConfigurer implements WebMvcConfigurer {

    @Bean
    public TokenCheckInterceptor tokenCheckInterceptor() {
        return new TokenCheckInterceptor();
    }
    @Bean
    public CrosInterceptor crosInterceptor() {
        return new CrosInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加一个拦截器，连接以/admin为前缀的 url路径
        //registry.addInterceptor(tokenCheckInterceptor()).addPathPatterns("/**/**");
        registry.addMapping("/**/**")
                .exposedHeaders("Authorization")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crosInterceptor()).addPathPatterns("/**/**");
        // 添加一个拦截器，连接以/admin为前缀的 url路径
        registry.addInterceptor(tokenCheckInterceptor()).addPathPatterns("/**/**");
    }
}
