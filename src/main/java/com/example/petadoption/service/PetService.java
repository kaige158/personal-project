package com.example.petadoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.petadoption.entity.Pet;

import java.util.List;

/**
 * 宠物业务逻辑接口
 */
public interface PetService extends IService<Pet> {

    /** 获取最新 N 只待领养宠物 */
    List<Pet> getLatest(int limit);

    /** 分页查询宠物列表（支持多条件筛选） */
    Page<Pet> pageQuery(long current, long size, Long categoryId, Integer gender, String keyword);

    /** 查询宠物详情（含图片列表、机构信息、分类名） */
    Pet getDetailById(Long id);

    /** 管理员新增宠物 */
    boolean addPet(Pet pet, List<String> imageUrls);

    /** 管理员更新宠物 */
    boolean updatePet(Pet pet, List<String> imageUrls);

    /** 管理员下架宠物 */
    boolean offPet(Long id);

    /** 获取宠物数量统计 */
    long countByStatus(Integer status);
}
