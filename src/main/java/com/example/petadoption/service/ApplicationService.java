package com.example.petadoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.petadoption.entity.AdoptionApplication;

/**
 * 领养申请业务逻辑接口
 */
public interface ApplicationService extends IService<AdoptionApplication> {

    /** 用户提交领养申请 */
    boolean submit(AdoptionApplication application);

    /** 管理员审核通过 */
    boolean approve(Long applicationId, String comment);

    /** 管理员驳回 */
    boolean reject(Long applicationId, String comment);

    /** 查询某用户的申请列表 */
    Page<AdoptionApplication> getUserApplications(long current, long size, Long userId);

    /** 查询全部申请（管理员） */
    Page<AdoptionApplication> getAllApplications(long current, long size, Integer status);

    /** 统计待审核数量 */
    long countByStatus(Integer status);
}
