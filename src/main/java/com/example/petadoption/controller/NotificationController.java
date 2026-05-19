package com.example.petadoption.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Notification;
import com.example.petadoption.entity.User;
import com.example.petadoption.mapper.NotificationMapper;
import com.example.petadoption.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationMapper notificationMapper;

    /** 获取当前用户的通知列表 */
    @GetMapping("/list")
    public Result<?> list(HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        if (loginUser == null) return Result.fail(401, "请先登录");

        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, loginUser.getId())
               .orderByDesc(Notification::getCreateTime);
        List<Notification> list = notificationMapper.selectList(wrapper);
        return Result.ok(list);
    }

    /** 标记已读 */
    @PostMapping("/read/{id}")
    public Result<?> read(@PathVariable Long id) {
        Notification n = new Notification();
        n.setId(id);
        n.setIsRead(1);
        notificationMapper.updateById(n);
        return Result.ok();
    }
}
