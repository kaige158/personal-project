package com.example.petadoption.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Pet;
import com.example.petadoption.entity.PetCategory;
import com.example.petadoption.entity.PetImage;
import com.example.petadoption.mapper.PetCategoryMapper;
import com.example.petadoption.mapper.PetImageMapper;
import com.example.petadoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物控制器（用户端浏览）
 */
@Controller
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;
    @Autowired
    private PetCategoryMapper petCategoryMapper;
    @Autowired
    private PetImageMapper petImageMapper;

    /**
     * 宠物列表页（带分类筛选、性别筛选、关键字搜索）
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "8") long size,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) Integer gender,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        // 查询所有分类（供筛选下拉框使用）
        List<PetCategory> categories = petCategoryMapper.selectList(null);
        model.addAttribute("categories", categories);

        // 分页查询宠物列表
        Page<Pet> petPage = petService.pageQuery(page, size, categoryId, gender, keyword);
        model.addAttribute("page", petPage);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("gender", gender);
        model.addAttribute("keyword", keyword);

        return "pet/list";
    }

    /**
     * 宠物详情页
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Pet pet = petService.getDetailById(id);
        if (pet == null) {
            return "redirect:/pet/list";
        }
        model.addAttribute("pet", pet);

        // 查询分类名
        if (pet.getCategoryId() != null) {
            PetCategory category = petCategoryMapper.selectById(pet.getCategoryId());
            if (category != null) {
                pet.setCategoryName(category.getName());
            }
        }

        // 查询照片列表
        LambdaQueryWrapper<PetImage> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(PetImage::getPetId, id).orderByAsc(PetImage::getSortOrder);
        List<PetImage> images = petImageMapper.selectList(imgWrapper);
        model.addAttribute("images", images);

        return "pet/detail";
    }

    /**
     * 切换收藏状态（AJAX）
     */
    @PostMapping("/toggleFavorite")
    @ResponseBody
    public Result<?> toggleFavorite(@RequestParam Long petId) {
        // 在第四阶段实现收藏模块时完善此方法
        return Result.ok();
    }
}
