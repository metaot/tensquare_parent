package com.tensquare.friend.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//声明使用Feign调用用户微服务
@FeignClient("tensquare-user")
public interface UserClient {

    //PUT /user/followcount/{userId}/{x} 修改关注数
    @RequestMapping(value = "user/followcount/{userId}/{x}", method = RequestMethod.PUT)
    public Result followcount(@PathVariable("userId") String userId,
                              @PathVariable("x") Integer x);


    //PUT /user/fanscount/{userId}/{x} 修改粉丝数
    @RequestMapping(value = "user/fanscount/{userId}/{x}", method = RequestMethod.PUT)
    public Result fanscount(@PathVariable("userId") String userId,
                            @PathVariable("x") Integer x);

}
