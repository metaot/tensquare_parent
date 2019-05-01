package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

//使用熔断器,编写备用方案,
//当被调用的服务不可用的时候,打开熔断器,使用这个备用方案
@Component
public class LabelClientImpl implements LabelClient {

    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR, "基础微服务不可用,启动熔断器");
    }
}
