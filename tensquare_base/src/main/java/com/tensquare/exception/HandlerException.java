package com.tensquare.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//回顾以前springMVC课程中,全局异常处理器
//@Component
public class HandlerException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {

        //自定义异常,运行时异常

        //if(ex instanceof RuntimeException)

        ModelAndView modelAndView = new ModelAndView();

        //设置异常提示页面(给一个友好的页面提示)

        //设置异常提示信息

        return null;
    }
}
