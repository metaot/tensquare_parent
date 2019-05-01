package com.tensquare.friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

@SpringBootApplication
//开启eureka客户端
@EnableEurekaClient
//发现Feign客户端
@EnableDiscoveryClient
//使用Feign客户端
@EnableFeignClients
public class FriendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FriendApplication.class, args);
    }

    //创建jwt工具类的实例
    @Bean
    public JwtUtil createJwtUtil() {
        return new JwtUtil();
    }
}
