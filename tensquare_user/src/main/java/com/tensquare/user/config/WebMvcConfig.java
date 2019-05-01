package com.tensquare.user.config;

import com.tensquare.user.intercept.MyIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private MyIntercept myIntercept;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myIntercept)
                .addPathPatterns("/**")//拦截器拦截的url,拦截所有
                .excludePathPatterns("/**/login");//拦截器不拦截的url,所有登录不拦截
    }
}
