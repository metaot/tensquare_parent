package com.tensquare.user.intercept;

import com.tensquare.user.util.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.ThrowableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyIntercept implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    //之前,在进入Controller方法之前,先执行这个拦截器的方法
    //如果返回false表示要进行拦截,就不会往后执行(Controller方法不会执行),如果返回的是true,表示放行,继续往后执行
    //使用场景:权限验证,登录验证
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //System.out.println("拦截器的:preHandle");

        //在进入Controller方法之前应该先进行身份鉴权操作,在拦截器进行token解析
        //从请求头中获取token的值
        String header = request.getHeader("Authorization");

        //判断请求头是否有值
        if (header == null || "".equals(header)) {
            //直接放行
            return true;
        }

        //判断是否以Bearer 开头
        if (!header.startsWith("Bearer ")) {
            return true;
        }


        try {
            //对token进行截取
            String token = header.substring(7);

            //把token解析为claims
            Claims claims = jwtUtil.parseJWT(token);

            //需求不一样,有的需要用户权限,有的需要用户的id....
            //可以直接传递claims,需要什么自己就可以获取
            //如何把拦截器解析出来的claims传递给Controller方法进行使用
            //1. 拦截器指定的逻辑和Controller方法执行的逻辑是一个请求中的范围,使用的是同一个request
            //可以把claims放到request进行传递
            //request.setAttribute("claims", claims);


            //2.用户的一个请求是一个线程,也可以把claims放到当前线程中,ThreadLocal也可以存放
            ThreadLocalUtil.set(claims);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //现在只是进行token解析,现在没有确定被拦截的Controller是否需要鉴权
        //全部放行,返回true
        return true;
    }

    //之中,在进入Controller方法,执行了逻辑之后,在返回ModelAndView结果之前,执行拦截器方法
    //使用场景:存放公共数据,例如存放所有页面要展示的活动信息
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //System.out.println("拦截器的:postHandle");
    }

    //之后,在进入Controller方法,执行了逻辑之后,在返回ModelAndView结果之后,该做的事全做完了,才执行这个之后方法
    //使用场景:记录最终的日志信息,处理异常
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //System.out.println("拦截器的:afterCompletion");
    }
}
