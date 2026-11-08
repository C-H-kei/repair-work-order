package com.dormitory.repairbackend.interceptor;

import com.dormitory.repairbackend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求（跨域预检）
        if ("OPTIONS".equals(request.getMethod())) return true;

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                JwtUtil.parseToken(token); // 验证通过
                return true; // 放行
            } catch (Exception e) {
                // Token 过期或无效
            }
        }

        // 拦截未登录
        response.setStatus(401);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token已过期\"}");
        return false;
    }
}