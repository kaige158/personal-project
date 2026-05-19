package com.example.petadoption.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.petadoption.common.Result;
import com.example.petadoption.entity.Article;
import com.example.petadoption.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 科普文章控制器（用户端浏览）
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    /** 文章列表 */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") long page,
                       @RequestParam(defaultValue = "10") long size,
                       Model model) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getCreateTime);
        Page<Article> articlePage = articleMapper.selectPage(new Page<>(page, size), wrapper);
        model.addAttribute("page", articlePage);
        return "article/list";
    }

    /** 文章详情 */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            return "redirect:/article/list";
        }
        // 浏览量+1
        Article updateView = new Article();
        updateView.setId(id);
        updateView.setViewCount(article.getViewCount() + 1);
        articleMapper.updateById(updateView);
        article.setViewCount(article.getViewCount() + 1);

        model.addAttribute("article", article);
        return "article/detail";
    }

    /** 点赞（AJAX） */
    @PostMapping("/like/{id}")
    @ResponseBody
    public Result<?> like(@PathVariable Long id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            Article update = new Article();
            update.setId(id);
            update.setLikeCount(article.getLikeCount() + 1);
            articleMapper.updateById(update);
            return Result.ok("点赞成功", article.getLikeCount() + 1);
        }
        return Result.fail("文章不存在");
    }
}
