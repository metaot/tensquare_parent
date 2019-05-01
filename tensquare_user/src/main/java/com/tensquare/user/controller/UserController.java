package com.tensquare.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensquare.user.util.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    //PUT /user/followcount/{userId}/{x} 修改关注数
    @RequestMapping(value = "followcount/{userId}/{x}", method = RequestMethod.PUT)
    public Result followcount(@PathVariable String userId,
                              @PathVariable Integer x) {
        userService.followcount(userId, x);

        return new Result(true, StatusCode.OK, "修改成功");
    }


    //PUT /user/fanscount/{userId}/{x} 修改粉丝数
    @RequestMapping(value = "fanscount/{userId}/{x}", method = RequestMethod.PUT)
    public Result fanscount(@PathVariable String userId,
                            @PathVariable Integer x) {
        //根据用户id修改粉丝数
        userService.fanscount(userId, x);

        //返回
        return new Result(true, StatusCode.OK, "修改成功");
    }

    //POST /user/login 登陆
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {

        User userLogin = userService.login(user);

        if (userLogin == null) {
            return new Result(false, StatusCode.LOGINERROR, "登录失败");
        }

        //用户登录成功,需要签发token(使用JWT规范)
        String token = jwtUtil.createJWT(userLogin.getId(), userLogin.getMobile(), "user");

        //封装token数据
        Map<String, String> map = new HashMap<>();

        map.put("mobile", userLogin.getMobile());
        map.put("token", token);

        return new Result(true, StatusCode.OK, "登录成功", map);
    }

    //POST /user/register/{code} 注册用户
    @RequestMapping(value = "register/{code}", method = RequestMethod.POST)
    public Result register(@PathVariable String code,
                           @RequestBody User user) {
        //判断短信验证码是否存在
        if (code == null || "".equals(code)) {
            //如果不存在则要求用户获取短信验证码
            return new Result(false, StatusCode.ERROR, "请获取短信验证码");
        }

        //判断短信验证码是否正确
        String redisCode = (String) redisTemplate.opsForValue().get("sms_" + user.getMobile());
        if (!code.equals(redisCode)) {
            //如果不正确,要求用户重新输入
            return new Result(false, StatusCode.ERROR, "验证码错误,请重新输入");
        }

        //执行用户注册操作
        userService.register(user);

        //返回结果
        return new Result(true, StatusCode.OK, "注册成功");
    }

    //POST /user/sendsms/{mobile} 发送手机验证码
    @RequestMapping(value = "sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);
        return new Result(true, StatusCode.OK, "短信验证码发送成功");
    }


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id, HttpServletRequest request) {
        //普通用户不能删除其他普通用户,只有管理员才可以删除其他用户
        //在删除用户之前,先进行鉴权操作,判断是否用户是管理员身份

        ////获取请求头中Authorization的内容
        //String header = request.getHeader("Authorization");
        //
        ////判断获取的头是否为空
        //if (header == null || "".equals(header)) {
        //    //如果为空表示没有携带token
        //    return new Result(false, StatusCode.ACCESSERROR, "用户未登录");
        //}
        //
        ////判断头是否是Bearer+空格开头
        //if (!header.startsWith("Bearer ")) {
        //    //如果不是Bearer+空格开头,表示token信息不合法
        //    return new Result(false, StatusCode.ACCESSERROR, "token不合法,请重新操作");
        //}
        //
        //try {
        //    //获取token的值,获取从角标7开始的字符串
        //    String token = header.substring(7);
        //
        //    //解析token,获取到claims,(处理异常)
        //    Claims claims = jwtUtil.parseJWT(token);

        //获取拦截器解析的claims
        //Claims claims = (Claims) request.getAttribute("claims");
        Claims claims = ThreadLocalUtil.get();

        //判断claims
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "token不合法,请重新登录");
        }

        //判断claims的roles是否为admin
        String roles = (String) claims.get("roles");

        if ("admin".equals(roles)) {
            //如果是admin,表示用户是管理员,就执行删除
            userService.deleteById(id);
        } else {
            //如果不是admin,表示用户不是管理员,就不能删除
            return new Result(false, StatusCode.ACCESSERROR, "用户没有删除权限");
        }
        //
        //} catch (Exception e) {
        //    e.printStackTrace();
        //    return new Result(false, StatusCode.ACCESSERROR, "token不合法,请重新登录");
        //}

        return new Result(true, StatusCode.OK, "操作成功");
    }

}
