package com.example.petadoption.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Organization;
import com.example.petadoption.mapper.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员机构管理控制器
 */
@Controller
@RequestMapping("/admin/organization")
public class AdminOrganizationController {

    @Autowired
    private OrganizationMapper organizationMapper;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "10") long size,
                       Model model) {
        Page<Organization> orgPage = organizationMapper.selectPage(new Page<>(page, size), null);
        model.addAttribute("page", orgPage);
        return "admin/organization-list";
    }

    @GetMapping("/add")
    public String addForm() {
        return "admin/organization-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("org", organizationMapper.selectById(id));
        return "admin/organization-form";
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(Organization org) {
        if (org.getId() == null) {
            organizationMapper.insert(org);
        } else {
            organizationMapper.updateById(org);
        }
        return Result.ok("保存成功");
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<?> delete(@PathVariable Long id) {
        organizationMapper.deleteById(id);
        return Result.ok("已删除");
    }
}
