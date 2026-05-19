package com.example.petadoption.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Favorite;
import com.example.petadoption.entity.Pet;
import com.example.petadoption.entity.PetImage;
import com.example.petadoption.entity.User;
import com.example.petadoption.mapper.FavoriteMapper;
import com.example.petadoption.mapper.PetImageMapper;
import com.example.petadoption.mapper.PetMapper;
import com.example.petadoption.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private PetMapper petMapper;
    @Autowired
    private PetImageMapper petImageMapper;

    /** 添加收藏 */
    @PostMapping("/add")
    public Result<?> add(@RequestParam Long petId, HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        if (loginUser == null) return Result.fail(401, "请先登录");

        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, loginUser.getId()).eq(Favorite::getPetId, petId);
        if (favoriteMapper.selectCount(wrapper) > 0) {
            return Result.fail("已收藏过该宠物");
        }

        Favorite fav = new Favorite();
        fav.setUserId(loginUser.getId());
        fav.setPetId(petId);
        favoriteMapper.insert(fav);
        return Result.ok("收藏成功");
    }

    /** 取消收藏 */
    @PostMapping("/remove")
    public Result<?> remove(@RequestParam Long petId, HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, loginUser.getId()).eq(Favorite::getPetId, petId);
        favoriteMapper.delete(wrapper);
        return Result.ok("已取消收藏");
    }

    /** 收藏列表 */
    @GetMapping("/list")
    public Result<?> list(HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        if (loginUser == null) return Result.fail(401, "请先登录");

        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, loginUser.getId()).orderByDesc(Favorite::getCreateTime);
        List<Favorite> favs = favoriteMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Favorite fav : favs) {
            Pet pet = petMapper.selectById(fav.getPetId());
            if (pet != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("petId", pet.getId());
                item.put("petName", pet.getName());
                item.put("petBreed", pet.getBreed());
                // 找封面图
                LambdaQueryWrapper<PetImage> imgW = new LambdaQueryWrapper<>();
                imgW.eq(PetImage::getPetId, pet.getId()).orderByAsc(PetImage::getSortOrder).last("LIMIT 1");
                PetImage img = petImageMapper.selectOne(imgW);
                item.put("coverImage", img != null ? img.getImageUrl() : null);
                result.add(item);
            }
        }
        return Result.ok(result);
    }
}
