package com.zyt.xxt.config;

import com.zyt.xxt.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "缺少 token");
            return false;
        }

        try {
            Claims claims = jwtUtils.parseToken(token);
            String username = claims.getSubject();

            // 验证 token 的有效性
            if (!jwtUtils.validateToken(token, username)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "无效 token");
                return false;
            }

            request.setAttribute("claims", claims); // 可选：将解析出的信息传递给 controller
        } catch (JwtException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "无效 token");
            return false;
        }

        return true;
    }
}
