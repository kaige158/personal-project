package com.example.petadoption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.petadoption.entity.User;

/**
 * 用户业务逻辑接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @return 注册成功返回用户对象，用户名已存在返回 null
     */
    User register(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 明文密码
     * @return 登录成功返回用户对象，失败返回 null
     */
    User login(String username, String password);

    /**
     * 修改个人信息（不包含密码）
     */
    boolean updateProfile(User user);

    /**
     * 修改密码
     * @param userId      用户 ID
     * @param oldPassword 旧密码（明文）
     * @param newPassword 新密码（明文）
     * @return 是否修改成功
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
}
