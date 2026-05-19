package com.example.petadoption.controller;

import com.example.petadoption.entity.Announcement;
import com.example.petadoption.entity.Pet;
import com.example.petadoption.service.AnnouncementService;
import com.example.petadoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 首页控制器
 */
@Controller
public class IndexController {

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private PetService petService;

    /**
     * 门户首页
     * 展示最新公告、最新上架宠物
     */
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        // 获取最新 5 条公告
        List<Announcement> announcements = announcementService.getLatest(5);
        model.addAttribute("announcements", announcements);

        // 获取最新 8 只待领养宠物
        List<Pet> latestPets = petService.getLatest(8);
        model.addAttribute("latestPets", latestPets);

        return "index";
    }
}
