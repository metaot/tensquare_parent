package com.tensquare.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

@SpringBootApplication
//开启微服务zuul网关,不要使用@EnableZuulServer
@EnableZuulProxy
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

    //创建jwt工具类实例
    @Bean
    public JwtUtil createJwtUtil() {
        return new JwtUtil();
    }
}
