package com.example.petadoption.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Article;
import com.example.petadoption.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 管理员文章管理控制器
 */
@Controller
@RequestMapping("/admin/article")
public class AdminArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "10") long size,
                       Model model) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getCreateTime);
        Page<Article> articlePage = articleMapper.selectPage(new Page<>(page, size), wrapper);
        model.addAttribute("page", articlePage);
        return "admin/article-list";
    }

    @GetMapping({"/add", "/edit/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("article", articleMapper.selectById(id));
        }
        return "admin/article-form";
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(Article article) {
        if (article.getId() == null) {
            article.setCreateTime(LocalDateTime.now());
            articleMapper.insert(article);
        } else {
            article.setUpdateTime(LocalDateTime.now());
            articleMapper.updateById(article);
        }
        return Result.ok("保存成功");
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<?> delete(@PathVariable Long id) {
        articleMapper.deleteById(id);
        return Result.ok("已删除");
    }
}
