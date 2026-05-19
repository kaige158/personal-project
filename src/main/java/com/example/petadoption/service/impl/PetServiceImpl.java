package com.example.petadoption.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.petadoption.common.Constants;
import com.example.petadoption.entity.Pet;
import com.example.petadoption.entity.PetImage;
import com.example.petadoption.mapper.PetImageMapper;
import com.example.petadoption.mapper.PetMapper;
import com.example.petadoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 宠物业务逻辑实现
 */
@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {

    @Autowired
    private PetImageMapper petImageMapper;

    @Override
    public List<Pet> getLatest(int limit) {
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pet::getStatus, Constants.PET_STATUS_AVAILABLE)
               .orderByDesc(Pet::getCreateTime)
               .last("LIMIT " + limit);
        List<Pet> pets = this.list(wrapper);
        // 为每只宠物设置封面图
        for (Pet pet : pets) {
            setCoverImage(pet);
        }
        return pets;
    }

    @Override
    public Page<Pet> pageQuery(long current, long size, Long categoryId, Integer gender, String keyword) {
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        // 只查待领养的宠物
        wrapper.eq(Pet::getStatus, Constants.PET_STATUS_AVAILABLE);
        // 按分类筛选
        if (categoryId != null) {
            wrapper.eq(Pet::getCategoryId, categoryId);
        }
        // 按性别筛选
        if (gender != null) {
            wrapper.eq(Pet::getGender, gender);
        }
        // 关键字模糊搜索（匹配名称或品种）
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(Pet::getName, keyword).or().like(Pet::getBreed, keyword));
        }
        wrapper.orderByDesc(Pet::getCreateTime);

        Page<Pet> page = this.page(new Page<>(current, size), wrapper);
        // 为每只宠物设置封面图
        for (Pet pet : page.getRecords()) {
            setCoverImage(pet);
        }
        return page;
    }

    @Override
    public Pet getDetailById(Long id) {
        Pet pet = this.getById(id);
        if (pet != null) {
            // 查询照片列表
            LambdaQueryWrapper<PetImage> imgWrapper = new LambdaQueryWrapper<>();
            imgWrapper.eq(PetImage::getPetId, id).orderByAsc(PetImage::getSortOrder);
            petImageMapper.selectList(imgWrapper); // 照片数据通过额外方法获取
        }
        return pet;
    }

    @Override
    @Transactional
    public boolean addPet(Pet pet, List<String> imageUrls) {
        // ① 保存宠物基本信息
        this.save(pet);
        // ② 保存宠物照片
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < imageUrls.size(); i++) {
                PetImage image = new PetImage();
                image.setPetId(pet.getId());
                image.setImageUrl(imageUrls.get(i));
                image.setSortOrder(i);
                petImageMapper.insert(image);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updatePet(Pet pet, List<String> imageUrls) {
        // ① 更新基本信息
        this.updateById(pet);
        if (imageUrls != null) {
            // ② 删除旧照片
            LambdaQueryWrapper<PetImage> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PetImage::getPetId, pet.getId());
            petImageMapper.delete(wrapper);
            // ③ 保存新照片
            for (int i = 0; i < imageUrls.size(); i++) {
                PetImage image = new PetImage();
                image.setPetId(pet.getId());
                image.setImageUrl(imageUrls.get(i));
                image.setSortOrder(i);
                petImageMapper.insert(image);
            }
        }
        return true;
    }

    @Override
    public boolean offPet(Long id) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setStatus(Constants.PET_STATUS_OFF);
        return this.updateById(pet);
    }

    @Override
    public long countByStatus(Integer status) {
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pet::getStatus, status);
        return this.count(wrapper);
    }

    /** 为宠物设置封面图（第一张照片的 URL） */
    private void setCoverImage(Pet pet) {
        LambdaQueryWrapper<PetImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetImage::getPetId, pet.getId())
               .orderByAsc(PetImage::getSortOrder)
               .last("LIMIT 1");
        PetImage image = petImageMapper.selectOne(wrapper);
        if (image != null) {
            pet.setCoverImage(image.getImageUrl());
        }
    }
}
