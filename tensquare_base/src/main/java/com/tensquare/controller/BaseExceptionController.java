package com.tensquare.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理器,另外一种处理异常的方式
@ControllerAdvice //表明异常处理的Controller
public class BaseExceptionController {

    //@ExceptionHandler表明处理异常的方法,可以在这里指定处理哪个异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handlerException(Exception e) {
        //把异常通知相关人员,使用邮件,手机短信,这里打印异常到控制台
        e.printStackTrace();

        //返回宜昌信息给接口的调用者
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
