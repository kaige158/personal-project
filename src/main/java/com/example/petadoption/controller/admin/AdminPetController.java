package com.example.petadoption.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Pet;
import com.example.petadoption.entity.PetCategory;
import com.example.petadoption.entity.Organization;
import com.example.petadoption.mapper.PetCategoryMapper;
import com.example.petadoption.mapper.OrganizationMapper;
import com.example.petadoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员宠物管理控制器
 */
@Controller
@RequestMapping("/admin/pet")
public class AdminPetController {

    @Autowired
    private PetService petService;
    @Autowired
    private PetCategoryMapper petCategoryMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    /** 宠物管理列表 */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "10") long size,
                       Model model) {
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Pet::getCreateTime);
        Page<Pet> petPage = petService.page(new Page<>(page, size), wrapper);
        model.addAttribute("page", petPage);
        return "admin/pet-list";
    }

    /** 新增/编辑宠物页面 */
    @GetMapping({"/add", "/edit/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        List<PetCategory> categories = petCategoryMapper.selectList(null);
        List<Organization> orgs = organizationMapper.selectList(null);
        model.addAttribute("categories", categories);
        model.addAttribute("orgs", orgs);

        if (id != null) {
            Pet pet = petService.getDetailById(id);
            model.addAttribute("pet", pet);
        }
        return "admin/pet-form";
    }

    /** 保存宠物（新增/编辑） */
    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(Pet pet, @RequestParam(required = false) List<String> images) {
        if (pet.getId() == null) {
            petService.addPet(pet, images);
        } else {
            petService.updatePet(pet, images);
        }
        return Result.ok("保存成功");
    }

    /** 下架宠物 */
    @PostMapping("/off/{id}")
    @ResponseBody
    public Result<?> off(@PathVariable Long id) {
        petService.offPet(id);
        return Result.ok("已下架");
    }
}
