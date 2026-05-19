package com.example.petadoption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库 user 表
 */
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;      // 用户名
    private String password;      // 密码（BCrypt 加密存储）
    private String realName;      // 真实姓名
    private String phone;         // 手机号
    private String email;         // 邮箱
    private String address;       // 地址
    private Integer role;         // 角色：0=普通用户, 1=管理员
    private Integer status;       // 状态：0=正常, 1=禁用
    private LocalDateTime createTime;  // 注册时间

    // ===== getter/setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
