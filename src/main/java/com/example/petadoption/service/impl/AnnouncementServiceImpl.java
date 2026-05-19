package com.example.petadoption.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.petadoption.entity.Announcement;
import com.example.petadoption.mapper.AnnouncementMapper;
import com.example.petadoption.service.AnnouncementService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告业务逻辑实现
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public List<Announcement> getLatest(int limit) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Announcement::getIsTop)     // 置顶的排前面
               .orderByDesc(Announcement::getCreateTime) // 再按时间排序
               .last("LIMIT " + limit);
        return this.list(wrapper);
    }

    @Override
    public Page<Announcement> pageQuery(long current, long size) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Announcement::getIsTop)
               .orderByDesc(Announcement::getCreateTime);
        return this.page(new Page<>(current, size), wrapper);
    }

    @Override
    public boolean addAnnouncement(Announcement announcement) {
        return this.save(announcement);
    }

    @Override
    public boolean updateAnnouncement(Announcement announcement) {
        return this.updateById(announcement);
    }

    @Override
    public boolean deleteAnnouncement(Long id) {
        return this.removeById(id);
    }
}
