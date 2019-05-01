package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取请求的容器
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取客户端的请求
        HttpServletRequest request = requestContext.getRequest();

        //判断请求方法是否为OPTIONS,这个请求方法是zuul网关使用的,
        //不要拦截zuul网关自己的调用
        if (request.getMethod().equals("OPTIONS")) {
            return null;
        }

        //管理员登录不能拦截
        String url = request.getRequestURL().toString();
        if (url.indexOf("/admin/login") > 0) {
            System.out.println("登陆页面" + url);
            return null;
        }

        //获取请求头Authorization
        String header = request.getHeader("Authorization");
        //判断请求头合规
        if (header != null && !"".equals(header) && header.startsWith("Bearer ")) {
            try {
                //解析token
                String token = header.substring(7);
                Claims claims = jwtUtil.parseJWT(token);

                if (claims != null) {
                    Object roles = claims.get("roles");
                    //判断token是否是管理员身份
                    if ("admin".equals(roles)) {
                        //如果是管理员,转发请求头,放行
                        requestContext.addZuulRequestHeader("Authorization", header);

                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //如果不是管理员,需要拦截
        //设置网关不会进行微服务的路由,相当于拦截
        requestContext.setSendZuulResponse(false);
        //设置相应状态码
        requestContext.setResponseStatusCode(401);
        //设置响应体数据
        //提示不能转发,没有权限
        requestContext.setResponseBody("用户身份不是管理员,不能访问后台系统");

        //声明响应类型和字符串编码
        requestContext.getResponse().setContentType("text/html;charset=utf-8");

        return null;
    }
}
