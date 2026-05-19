package com.example.petadoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.petadoption.entity.Announcement;

import java.util.List;

/**
 * 公告业务逻辑接口
 */
public interface AnnouncementService extends IService<Announcement> {

    /** 获取最新 N 条公告 */
    List<Announcement> getLatest(int limit);

    /** 分页查询公告 */
    Page<Announcement> pageQuery(long current, long size);

    /** 管理员新增公告 */
    boolean addAnnouncement(Announcement announcement);

    /** 管理员更新公告 */
    boolean updateAnnouncement(Announcement announcement);

    /** 管理员删除公告 */
    boolean deleteAnnouncement(Long id);
}
