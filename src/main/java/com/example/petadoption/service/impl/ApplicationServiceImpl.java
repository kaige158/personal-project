package com.example.petadoption.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.petadoption.common.Constants;
import com.example.petadoption.entity.*;
import com.example.petadoption.mapper.*;
import com.example.petadoption.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 领养申请业务逻辑实现
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<AdoptionApplicationMapper, AdoptionApplication> implements ApplicationService {

    @Autowired
    private AdoptionApplicationMapper applicationMapper;
    @Autowired
    private PetMapper petMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private AdoptionAgreementMapper agreementMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean submit(AdoptionApplication application) {
        application.setStatus(Constants.APP_STATUS_PENDING);
        application.setCreateTime(LocalDateTime.now());
        return this.save(application);
    }

    @Override
    @Transactional
    public boolean approve(Long applicationId, String comment) {
        // ① 更新申请表状态为"已通过"
        AdoptionApplication app = new AdoptionApplication();
        app.setId(applicationId);
        app.setStatus(Constants.APP_STATUS_APPROVED);
        app.setAuditComment(comment);
        app.setAuditTime(LocalDateTime.now());
        this.updateById(app);

        // ② 更新宠物状态为"已领养"
        AdoptionApplication fullApp = this.getById(applicationId);
        Pet pet = new Pet();
        pet.setId(fullApp.getPetId());
        pet.setStatus(Constants.PET_STATUS_ADOPTED);
        petMapper.updateById(pet);

        // ③ 生成领养协议
        AdoptionAgreement agreement = new AdoptionAgreement();
        agreement.setApplicationId(applicationId);
        agreement.setUserId(fullApp.getUserId());
        agreement.setPetId(fullApp.getPetId());
        agreement.setContent("领养协议正文：用户同意遵守宠物领养相关规定，承诺善待宠物，定期接受回访。");
        agreement.setUserSign(Constants.AGREEMENT_UNSIGNED);
        agreement.setCreateTime(LocalDateTime.now());
        agreementMapper.insert(agreement);

        // ④ 发送通知给申请人
        Notification notification = new Notification();
        notification.setUserId(fullApp.getUserId());
        notification.setTitle("领养申请已通过");
        notification.setContent("恭喜！您对宠物(ID:" + fullApp.getPetId() + ")的领养申请已通过审核。请前往个人中心签署领养协议。" +
                (comment != null && !comment.isEmpty() ? " 审核意见：" + comment : ""));
        notification.setIsRead(Constants.NOTIFICATION_UNREAD);
        notification.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notification);

        return true;
    }

    @Override
    @Transactional
    public boolean reject(Long applicationId, String comment) {
        // ① 更新申请表状态为"已驳回"
        AdoptionApplication app = new AdoptionApplication();
        app.setId(applicationId);
        app.setStatus(Constants.APP_STATUS_REJECTED);
        app.setAuditComment(comment);
        app.setAuditTime(LocalDateTime.now());
        this.updateById(app);

        // ② 发送通知给申请人
        AdoptionApplication fullApp = this.getById(applicationId);
        Notification notification = new Notification();
        notification.setUserId(fullApp.getUserId());
        notification.setTitle("领养申请被驳回");
        notification.setContent("很遗憾，您对宠物(ID:" + fullApp.getPetId() + ")的领养申请未被通过。" +
                (comment != null && !comment.isEmpty() ? " 驳回原因：" + comment : ""));
        notification.setIsRead(Constants.NOTIFICATION_UNREAD);
        notification.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notification);

        return true;
    }

    @Override
    public Page<AdoptionApplication> getUserApplications(long current, long size, Long userId) {
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptionApplication::getUserId, userId)
               .orderByDesc(AdoptionApplication::getCreateTime);
        Page<AdoptionApplication> page = this.page(new Page<>(current, size), wrapper);
        // 填充分页记录中的关联信息
        for (AdoptionApplication app : page.getRecords()) {
            fillAssociation(app);
        }
        return page;
    }

    @Override
    public Page<AdoptionApplication> getAllApplications(long current, long size, Integer status) {
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(AdoptionApplication::getStatus, status);
        }
        wrapper.orderByAsc(AdoptionApplication::getStatus)     // 待审核的排前面
               .orderByDesc(AdoptionApplication::getCreateTime);
        Page<AdoptionApplication> page = this.page(new Page<>(current, size), wrapper);
        for (AdoptionApplication app : page.getRecords()) {
            fillAssociation(app);
        }
        return page;
    }

    @Override
    public long countByStatus(Integer status) {
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptionApplication::getStatus, status);
        return this.count(wrapper);
    }

    /** 填充申请记录中的关联信息（用户名、宠物名） */
    private void fillAssociation(AdoptionApplication app) {
        if (app.getUserId() != null) {
            User user = userMapper.selectById(app.getUserId());
            if (user != null) {
                app.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
                app.setUserPhone(user.getPhone());
            }
        }
        if (app.getPetId() != null) {
            Pet pet = petMapper.selectById(app.getPetId());
            if (pet != null) {
                app.setPetName(pet.getName());
            }
        }
    }
}
