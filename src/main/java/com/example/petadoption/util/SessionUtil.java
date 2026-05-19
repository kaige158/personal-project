package com.example.petadoption.util;

import com.example.petadoption.common.Constants;
import com.example.petadoption.entity.User;

import javax.servlet.http.HttpSession;

/**
 * Session 操作封装
 * 统一对 session 的读写，避免各处重复写 session key
 */
public class SessionUtil {

    /**
     * 将用户信息存入 session
     */
    public static void setLoginUser(HttpSession session, User user) {
        session.setAttribute(Constants.SESSION_USER, user);
    }

    /**
     * 从 session 获取当前登录用户
     * @return 用户对象，未登录返回 null
     */
    public static User getLoginUser(HttpSession session) {
        return (User) session.getAttribute(Constants.SESSION_USER);
    }

    /**
     * 清除 session 中的登录信息（退出登录）
     */
    public static void removeLoginUser(HttpSession session) {
        session.removeAttribute(Constants.SESSION_USER);
    }

    /**
     * 判断当前是否已登录
     */
    public static boolean isLogin(HttpSession session) {
        return getLoginUser(session) != null;
    }

    /**
     * 判断当前用户是否为管理员
     */
    public static boolean isAdmin(HttpSession session) {
        User user = getLoginUser(session);
        return user != null && user.getRole() == Constants.ROLE_ADMIN;
    }
}
