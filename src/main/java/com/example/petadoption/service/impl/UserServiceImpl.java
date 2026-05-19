package com.example.petadoption.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.petadoption.common.Constants;
import com.example.petadoption.entity.User;
import com.example.petadoption.mapper.UserMapper;
import com.example.petadoption.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户业务逻辑实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(User user) {
        // ① 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (this.count(wrapper) > 0) {
            return null;  // 用户名已被占用
        }

        // ② 密码 BCrypt 加密后存储（不能存明文）
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Constants.ROLE_USER);        // 默认注册为普通用户
        user.setStatus(Constants.USER_STATUS_NORMAL);
        user.setCreateTime(LocalDateTime.now());

        // ③ 保存到数据库
        this.save(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        // ① 根据用户名查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);

        if (user == null) {
            return null;  // 用户名不存在
        }
        if (user.getStatus() == Constants.USER_STATUS_DISABLED) {
            return null;  // 账号已被禁用
        }

        // ② 用 BCrypt 验证密码（用户输入的明文 vs 数据库中的密文）
        if (passwordEncoder.matches(password, user.getPassword())) {
            // 登录成功，不要把密码带回前端，置空
            user.setPassword(null);
            return user;
        }
        return null;  // 密码错误
    }

    @Override
    public boolean updateProfile(User user) {
        // 只更新可修改字段，不更新密码和角色
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setRealName(user.getRealName());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setAddress(user.getAddress());
        return this.updateById(updateUser);
    }

    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        // ① 查出用户，验证旧密码
        User user = this.getById(userId);
        if (user == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;  // 旧密码错误
        }

        // ② 更新为新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        return this.updateById(updateUser);
    }
}
