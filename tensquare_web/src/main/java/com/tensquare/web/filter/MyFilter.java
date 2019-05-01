package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyFilter extends ZuulFilter {
    @Override
    public String filterType() {
        //返回pre表示在进行微服务路由(转发,调用)之前,执行过滤器
        return "pre";
        //pre ：可以在请求被路由之前调用
        //route ：在路由请求时候被调用
        //post ：在route和error过滤器之后被调用
        //error ：处理请求时发生错误时被调用
    }

    @Override
    public int filterOrder() {
        //返回int型,设置过滤器的执行优先级
        //如果为0,表示当前的过滤器是最先执行的
        //数字越大,优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //当前网关是否执行当前的过滤器
        //返回true表示使用当前过滤器,false表示不使用
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //编写过滤器的逻辑
        System.out.println("zuul过滤器执行了");
        //使用网关进行路由后,请求头的数据,路由后丢失了
        //鉴权的token存放到请求头中,不能丢失头,应该进行转发

        //本例是前台网关,只需要把用户身份的头转发即可,不需要鉴权
        //前台系统查看头条文章,问答,活动等页面是不需要验证身份,所有人都能看

        //获取用户原来的请求头数据
        //先获取网关中存放请求的容器
        RequestContext requestContext = RequestContext.getCurrentContext();

        //从容器中获取用户的请求
        HttpServletRequest request = requestContext.getRequest();

        //从请求中获取请求头
        String header = request.getHeader("Authorization");

        //放到网关路由微服务的请求中
        requestContext.addZuulRequestHeader("Authorization", header);

        //表示方法结束,过滤器执行完成,放行
        return null;
    }
}
