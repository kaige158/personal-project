package com.example.petadoption.interceptor;

import com.example.petadoption.common.Constants;
import com.example.petadoption.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 管理员权限拦截器
 * 检查当前用户是否为管理员角色，不是则返回 403
 * 前提：该拦截器在 LoginInterceptor 之后执行，此时 session 中一定有 user
 */
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        if (user == null || user.getRole() != Constants.ROLE_ADMIN) {
            // 不是管理员 → 返回 403 禁止访问
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权访问管理后台");
            return false;
        }
        return true;
    }
}
