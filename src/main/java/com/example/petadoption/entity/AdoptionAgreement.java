package com.example.petadoption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 领养协议实体
 */
@TableName("adoption_agreement")
public class AdoptionAgreement {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long applicationId;
    private Long userId;
    private Long petId;
    private String content;
    private Integer userSign;
    private LocalDateTime signTime;
    private LocalDateTime createTime;

    // 关联字段
    private String userName;
    private String petName;
    private Integer applicationStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getUserSign() { return userSign; }
    public void setUserSign(Integer userSign) { this.userSign = userSign; }
    public LocalDateTime getSignTime() { return signTime; }
    public void setSignTime(LocalDateTime signTime) { this.signTime = signTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
    public Integer getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(Integer applicationStatus) { this.applicationStatus = applicationStatus; }
}
