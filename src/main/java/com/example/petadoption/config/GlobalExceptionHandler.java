package com.example.petadoption.config;

import com.example.petadoption.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * 统一捕获和处理控制器层抛出的异常，避免用户看到 500 错误页面
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有未捕获的异常
     * AJAX 请求：返回 JSON 错误信息
     * 页面请求：跳转错误页
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception e) {
        // 判断是否为 AJAX 请求
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header);

        if (isAjax) {
            return Result.fail("服务器内部错误：" + e.getMessage());
        }
        // 页面请求 → 跳转错误页（简单返回文本）
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        mav.addObject("message", e.getMessage());
        return mav;
    }
}
