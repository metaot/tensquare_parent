package com.tensquare.qa.intercept;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyIntercept implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");

        if (header == null || "".equals(header) || !header.startsWith("Bearer ")) {
            return true;
        }

        try {
            String token = header.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            request.setAttribute("claims", claims);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
