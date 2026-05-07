package com.typecho.controller;

import com.typecho.common.Result;
import com.typecho.dto.LoginDTO;
import com.typecho.dto.RegisterDTO;
import com.typecho.entity.User;
import com.typecho.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录、注册、认证相关接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "通过用户名和密码登录，返回JWT令牌")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        String token = authService.login(dto);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenType", "Bearer");
        return Result.success("登录成功", data);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public Result<Long> register(@Valid @RequestBody RegisterDTO dto) {
        Long uid = authService.register(dto);
        return Result.success("注册成功", uid);
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的信息")
    public Result<User> getCurrentUser() {
        User user = authService.getCurrentUser();
        // 清除敏感信息
        user.setPassword(null);
        user.setAuthCode(null);
        return Result.success(user);
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码", description = "修改当前用户密码")
    public Result<Void> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        authService.updatePassword(oldPassword, newPassword);
        return Result.success("密码修改成功", null);
    }
}
