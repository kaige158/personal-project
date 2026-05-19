package com.example.petadoption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公告控制器
 * 公告直接在首页展示，此控制器仅用于将来扩展
 */
@Controller
@RequestMapping("/announcement")
public class AnnouncementController {

    @GetMapping("/list")
    public String list() {
        // 公告功能已集成在首页，此处预留
        return "redirect:/";
    }
}
