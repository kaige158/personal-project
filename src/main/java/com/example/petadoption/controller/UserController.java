package com.example.petadoption.controller;

import com.example.petadoption.common.Result;
import com.example.petadoption.entity.User;
import com.example.petadoption.service.UserService;
import com.example.petadoption.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 用户控制器
 * 处理登录、注册、个人信息相关请求
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // ===== 页面跳转 =====

    /** 登录页面 */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /** 注册页面 */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /** 个人中心页面（需登录） */
    @GetMapping("/user/center")
    public String center(Model model, HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        model.addAttribute("user", loginUser);
        return "user/center";
    }

    /** 修改个人信息页面 */
    @GetMapping("/user/edit")
    public String editProfile(Model model, HttpSession session) {
        model.addAttribute("user", SessionUtil.getLoginUser(session));
        return "user/edit";
    }

    /** 修改密码页面 */
    @GetMapping("/user/password")
    public String changePassword() {
        return "user/password";
    }

    /** 我的申请页面 */
    @GetMapping("/user/my-applications")
    public String myApplications() {
        return "user/my-applications";
    }

    /** 我的收藏页面 */
    @GetMapping("/user/my-favorites")
    public String myFavorites() {
        return "user/my-favorites";
    }

    /** 消息通知页面 */
    @GetMapping("/user/my-notifications")
    public String myNotifications() {
        return "user/my-notifications";
    }

    /** 领养协议页面 */
    @GetMapping("/user/agreement")
    public String agreement() {
        return "user/agreement";
    }

    // ===== 业务操作 =====

    /**
     * 注册处理
     */
    @PostMapping("/doRegister")
    @ResponseBody
    public Result<?> doRegister(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String realName,
                                @RequestParam String phone) {
        // ① 简单校验非空
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return Result.fail("用户名和密码不能为空");
        }
        if (password.length() < 6) {
            return Result.fail("密码长度不能少于6位");
        }

        // ② 构建用户对象
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setRealName(realName);
        user.setPhone(phone);

        // ③ 执行注册
        User result = userService.register(user);
        if (result == null) {
            return Result.fail("用户名已存在");
        }
        return Result.ok("注册成功，请登录");
    }

    /**
     * 登录处理
     */
    @PostMapping("/doLogin")
    @ResponseBody
    public Result<?> doLogin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session) {
        User user = userService.login(username, password);
        if (user == null) {
            return Result.fail("用户名或密码错误");
        }
        // 登录成功 → 用户信息写入 session
        SessionUtil.setLoginUser(session, user);
        return Result.ok("登录成功", user);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionUtil.removeLoginUser(session);
        return "redirect:/";
    }

    /**
     * 更新个人信息
     */
    @PostMapping("/user/updateProfile")
    @ResponseBody
    public Result<?> updateProfile(@RequestParam String realName,
                                   @RequestParam String phone,
                                   @RequestParam String email,
                                   @RequestParam String address,
                                   HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
        loginUser.setRealName(realName);
        loginUser.setPhone(phone);
        loginUser.setEmail(email);
        loginUser.setAddress(address);

        if (userService.updateProfile(loginUser)) {
            // 同步更新 session 中的用户信息
            SessionUtil.setLoginUser(session, loginUser);
            return Result.ok("修改成功");
        }
        return Result.fail("修改失败");
    }

    /**
     * 修改密码
     */
    @PostMapping("/user/updatePassword")
    @ResponseBody
    public Result<?> updatePassword(@RequestParam String oldPassword,
                                    @RequestParam String newPassword,
                                    HttpSession session) {
        if (newPassword.length() < 6) {
            return Result.fail("新密码长度不能少于6位");
        }
        User loginUser = SessionUtil.getLoginUser(session);
        boolean success = userService.updatePassword(loginUser.getId(), oldPassword, newPassword);
        if (success) {
            // 密码修改成功，强制重新登录
            SessionUtil.removeLoginUser(session);
            return Result.ok("密码修改成功，请重新登录");
        }
        return Result.fail("旧密码错误");
    }
}
