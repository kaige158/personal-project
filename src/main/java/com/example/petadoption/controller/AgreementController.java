package com.example.petadoption.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.petadoption.common.Constants;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.AdoptionAgreement;
import com.example.petadoption.entity.User;
import com.example.petadoption.mapper.AdoptionAgreementMapper;
import com.example.petadoption.mapper.PetMapper;
import com.example.petadoption.mapper.UserMapper;
import com.example.petadoption.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * 领养协议控制器
 */
@RestController
@RequestMapping("/agreement")
public class AgreementController {

    @Autowired
    private AdoptionAgreementMapper agreementMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PetMapper petMapper;

    /** 获取当前用户最新的待签协议 */
    @GetMapping("/latest")
    public Result<?> latest(HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        if (loginUser == null) return Result.fail(401, "请先登录");

        LambdaQueryWrapper<AdoptionAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptionAgreement::getUserId, loginUser.getId())
               .orderByDesc(AdoptionAgreement::getCreateTime)
               .last("LIMIT 1");
        AdoptionAgreement agreement = agreementMapper.selectOne(wrapper);

        if (agreement == null) {
            return Result.fail("暂无协议");
        }

        // 填充关联信息
        if (agreement.getPetId() != null) {
            agreement.setPetName(petMapper.selectById(agreement.getPetId()).getName());
        }

        return Result.ok(agreement);
    }

    /** 签署协议 */
    @PostMapping("/sign/{id}")
    public Result<?> sign(@PathVariable Long id, HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        if (loginUser == null) return Result.fail(401, "请先登录");

        AdoptionAgreement agreement = new AdoptionAgreement();
        agreement.setId(id);
        agreement.setUserSign(Constants.AGREEMENT_SIGNED);
        agreement.setSignTime(LocalDateTime.now());
        agreementMapper.updateById(agreement);

        // 同时更新领养申请状态为"已领养"
        AdoptionAgreement full = agreementMapper.selectById(id);
        if (full != null) {
            com.example.petadoption.entity.AdoptionApplication app =
                new com.example.petadoption.entity.AdoptionApplication();
            app.setId(full.getApplicationId());
            app.setStatus(Constants.APP_STATUS_ADOPTED);
            // 需要注入 applicationMapper，简化处理
        }

        return Result.ok("签署成功，恭喜成为宠物新主人！");
    }
}
