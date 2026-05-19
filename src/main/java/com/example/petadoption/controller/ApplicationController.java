package com.example.petadoption.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.AdoptionApplication;
import com.example.petadoption.entity.User;
import com.example.petadoption.service.ApplicationService;
import com.example.petadoption.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 领养申请控制器
 */
@Controller
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 提交领养申请
     */
    @PostMapping("/submit")
    @ResponseBody
    public Result<?> submit(@RequestParam Long petId,
                            @RequestParam String occupation,
                            @RequestParam String housingType,
                            @RequestParam(defaultValue = "0") Integer hasExperience,
                            @RequestParam String reason,
                            HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        if (loginUser == null) {
            return Result.fail(401, "请先登录");
        }

        AdoptionApplication app = new AdoptionApplication();
        app.setUserId(loginUser.getId());
        app.setPetId(petId);
        app.setOccupation(occupation);
        app.setHousingType(housingType);
        app.setHasExperience(hasExperience);
        app.setReason(reason);

        if (applicationService.submit(app)) {
            return Result.ok("申请提交成功，请等待管理员审核");
        }
        return Result.fail("提交失败");
    }

    /**
     * 我的申请列表
     */
    @GetMapping("/my-applications")
    public String myApplications(@RequestParam(defaultValue = "1") long page,
                                 @RequestParam(defaultValue = "10") long size,
                                 HttpSession session, Model model) {
        User loginUser = SessionUtil.getLoginUser(session);
        Page<AdoptionApplication> appPage = applicationService.getUserApplications(page, size, loginUser.getId());
        model.addAttribute("page", appPage);
        return "user/my-applications";
    }
}
