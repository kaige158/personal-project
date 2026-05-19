package com.example.petadoption.config;

import com.example.petadoption.interceptor.AdminInterceptor;
import com.example.petadoption.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * ① 注册拦截器（登录拦截 → 管理员权限拦截，按顺序执行）
 * ② 配置文件上传的虚拟路径映射
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 注册拦截器
     * 执行顺序：先 LoginInterceptor → 再 AdminInterceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // ① 登录拦截器：除了登录、注册、首页、静态资源外，其他页面需要登录
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")                          // 拦截所有请求
                .excludePathPatterns(                            // 排除不需要登录的路径
                        "/", "/index",                          // 首页
                        "/login", "/doLogin",                   // 登录
                        "/register", "/doRegister",             // 注册
                        "/pet/list", "/pet/detail/**",          // 宠物浏览
                        "/article/**",                           // 科普文章浏览
                        "/organization/**",                      // 机构浏览
                        "/announcement/**",                      // 公告
                        "/css/**", "/js/**", "/images/**", "/uploads/**",  // 静态资源
                        "/error"                                 // 错误页面
                );

        // ② 管理员拦截器：匹配 /admin/** 的请求，检查是否为管理员
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**");                   // 只拦截管理后台
    }

    /**
     * 配置文件上传的虚拟路径
     * 访问 /uploads/xxx.jpg → 实际读取本地 uploads/ 目录下的文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
