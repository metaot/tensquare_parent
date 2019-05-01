package com.tensquare.qa.client;


import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//使用Feign客户端调用服务,参数是调用哪个服务(服务名字)
//fallback:配置当打开熔断器的时候,使用的备用方案是什么
@FeignClient(value = "tensquare-base", fallback = LabelClientImpl.class)
public interface LabelClient {

    //1. 把需要调用的微服务接口的方法复制过来
    //2. 修改方法:
    // value必须是完整的请求地址
    // @PathVariable必须填写占位符的名字
    @RequestMapping(value = "label/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);

}
