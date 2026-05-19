package com.example.petadoption.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.AdoptionApplication;
import com.example.petadoption.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员领养审核控制器
 */
@Controller
@RequestMapping("/admin/application")
public class AdminApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /** 申请审核列表 */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "10") long size,
                       @RequestParam(required = false) Integer status,
                       Model model) {
        Page<AdoptionApplication> appPage = applicationService.getAllApplications(page, size, status);
        model.addAttribute("page", appPage);
        model.addAttribute("status", status);
        return "admin/application-list";
    }

    /** 审核通过 */
    @PostMapping("/approve/{id}")
    @ResponseBody
    public Result<?> approve(@PathVariable Long id, @RequestParam(defaultValue = "") String comment) {
        applicationService.approve(id, comment);
        return Result.ok("审核通过");
    }

    /** 驳回 */
    @PostMapping("/reject/{id}")
    @ResponseBody
    public Result<?> reject(@PathVariable Long id, @RequestParam(defaultValue = "") String comment) {
        applicationService.reject(id, comment);
        return Result.ok("已驳回");
    }
}
