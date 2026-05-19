package com.example.petadoption.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.entity.Organization;
import com.example.petadoption.mapper.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 救助机构控制器（用户端浏览）
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationMapper organizationMapper;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "10") long size,
                       Model model) {
        Page<Organization> orgPage = organizationMapper.selectPage(new Page<>(page, size), null);
        model.addAttribute("page", orgPage);
        return "organization/list";
    }
}
