package com.example.petadoption.controller.admin;

import com.example.petadoption.common.Constants;
import com.example.petadoption.service.ApplicationService;
import com.example.petadoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理员后台首页（数据统计看板）
 */
@Controller
@RequestMapping("/admin")
public class AdminIndexController {

    @Autowired
    private PetService petService;
    @Autowired
    private ApplicationService applicationService;

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        // 统计数据
        model.addAttribute("totalPets", petService.count());
        model.addAttribute("availablePets", petService.countByStatus(Constants.PET_STATUS_AVAILABLE));
        model.addAttribute("adoptedPets", petService.countByStatus(Constants.PET_STATUS_ADOPTED));
        model.addAttribute("pendingApps", applicationService.countByStatus(Constants.APP_STATUS_PENDING));
        model.addAttribute("approvedApps", applicationService.countByStatus(Constants.APP_STATUS_APPROVED));
        return "admin/index";
    }
}
