package com.typecho.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.common.PageResult;
import com.typecho.common.Result;
import com.typecho.dto.UserUpdateDTO;
import com.typecho.entity.User;
import com.typecho.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/{uid}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public Result<User> getById(@PathVariable Long uid) {
        User user = userService.getById(uid);
        if (user != null) {
            user.setPassword(null);
            user.setAuthCode(null);
        }
        return Result.success(user);
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有用户", description = "获取所有用户列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<User>> listAll() {
        List<User> users = userService.listAll();
        users.forEach(u -> {
            u.setPassword(null);
            u.setAuthCode(null);
        });
        return Result.success(users);
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取用户", description = "分页获取用户列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<User>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Page<User> page = userService.page(pageNum, pageSize, keyword);
        page.getRecords().forEach(u -> {
            u.setPassword(null);
            u.setAuthCode(null);
        });
        return Result.success(PageResult.of(page));
    }

    @PutMapping("/{uid}")
    @Operation(summary = "更新用户信息", description = "更新用户信息")
    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.user.uid")
    public Result<Void> update(@PathVariable Long uid, @RequestBody UserUpdateDTO dto) {
        dto.setUid(uid);
        userService.updateProfile(dto);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{uid}")
    @Operation(summary = "删除用户", description = "删除指定用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long uid) {
        userService.delete(uid);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> uids) {
        userService.batchDelete(uids);
        return Result.success("批量删除成功", null);
    }
}
