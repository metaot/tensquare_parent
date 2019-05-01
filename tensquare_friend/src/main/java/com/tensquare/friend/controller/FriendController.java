package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserClient userClient;

    //DELETE /friend/{friendid} 删除好友
    @RequestMapping(value = "{friendid}", method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid, HttpServletRequest request) {
        //获取自己的id
        Claims claims = (Claims) request.getAttribute("claims");

        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "用户没有登录");
        }

        String userid = claims.getId();
        //删除好友
        friendService.deleteFriend(userid, friendid);

        //修改用户的关注数和粉丝数
        //自己的关注数减一
        userClient.followcount(userid, -1);
        //别人的粉丝数减一
        userClient.fanscount(friendid, -1);

        //返回结果
        return new Result(true, StatusCode.OK, "删除好友成功");
    }


    //PUT /friend/like/{friendid}/{type} 添加好友或非好友
    @RequestMapping(value = "like/{friendid}/{type}", method = RequestMethod.PUT)
    public Result like(@PathVariable String friendid,
                       @PathVariable String type,
                       HttpServletRequest request) {
        //获取自己的用户id,只有登录后的用户才能使用好友操作
        //这里需要使用微服务鉴权,从request中获取解析结果
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "用户没有登录");
        }

        //获取自己的用户id
        String userId = claims.getId();

        //判断是添加好友,还是添加非好友  1:喜欢 2：不喜欢
        if ("1".equals(type)) {
            //如果为1,表示是添加好友,返回Boolean,如果已经添加好友返回true,如果没有添加好友,添加好友并返回false
            Boolean bool = friendService.addFriend(userId, friendid);

            //如果已经添加好友,不能再次添加好友
            if (bool) {
                return new Result(false, StatusCode.REPERROR, "不能重复添加好友");
            }

            //好友如果添加成功,修改用户的粉丝数和关注数
            //修改自己的关注数加一
            userClient.followcount(userId, 1);
            //修改别人的分数数加一
            userClient.fanscount(friendid, 1);


        } else {
            //真正的做法应该是判断2是添加非好友,如果type不是1也不是2,抛参数错误的异常
            //如果不是1,表示添加非好友
            friendService.addNoFriend(userId, friendid);
        }


        //返回结果
        return new Result(true, StatusCode.OK, "操作成功");

    }
}
