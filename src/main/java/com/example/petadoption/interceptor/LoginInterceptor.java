package com.example.petadoption.interceptor;

import com.example.petadoption.common.Constants;
import com.example.petadoption.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 * 检查 session 中是否有登录用户信息，没有则重定向到登录页
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        if (user == null) {
            // 未登录 → 重定向到登录页
            response.sendRedirect("/login");
            return false;
        }
        // 已登录 → 放行
        return true;
    }
}
