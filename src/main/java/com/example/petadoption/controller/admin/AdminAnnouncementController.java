package com.example.petadoption.controller.admin;

import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Announcement;
import com.example.petadoption.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员公告管理控制器
 */
@Controller
@RequestMapping("/admin/announcement")
public class AdminAnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "10") long size,
                       Model model) {
        model.addAttribute("page", announcementService.pageQuery(page, size));
        return "admin/announcement-list";
    }

    @GetMapping({"/add", "/edit/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("announcement", announcementService.getById(id));
        }
        return "admin/announcement-form";
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(Announcement announcement) {
        if (announcement.getId() == null) {
            announcementService.addAnnouncement(announcement);
        } else {
            announcementService.updateAnnouncement(announcement);
        }
        return Result.ok("保存成功");
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<?> delete(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return Result.ok("已删除");
    }
}
