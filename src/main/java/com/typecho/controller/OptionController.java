package com.typecho.controller;

import com.typecho.common.Result;
import com.typecho.entity.Option;
import com.typecho.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
@Tag(name = "系统配置管理", description = "系统配置相关接口")
public class OptionController {

    private final OptionService optionService;

    @GetMapping("/{name}")
    @Operation(summary = "获取配置项", description = "根据名称获取配置项")
    public Result<Option> getByName(@PathVariable String name) {
        Option option = optionService.getByName(name);
        return Result.success(option);
    }

    @GetMapping("/value/{name}")
    @Operation(summary = "获取配置值", description = "根据名称获取配置值")
    public Result<String> getValue(@PathVariable String name) {
        String value = optionService.getValue(name);
        return Result.success(value);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有配置", description = "获取所有系统配置")
    public Result<Map<String, String>> getAllOptions() {
        Map<String, String> options = optionService.getAllOptions();
        return Result.success(options);
    }

    @GetMapping("/user/{user}")
    @Operation(summary = "获取用户配置", description = "获取指定用户的配置")
    public Result<List<Option>> listByUser(@PathVariable Integer user) {
        List<Option> options = optionService.listByUser(user);
        return Result.success(options);
    }

    @PostMapping
    @Operation(summary = "设置配置", description = "设置单个配置项")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> setOption(
            @Parameter(description = "配置名称") @RequestParam String name,
            @Parameter(description = "配置值") @RequestParam String value) {
        optionService.setOption(name, value);
        return Result.success("设置成功", null);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量设置配置", description = "批量设置配置项")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> setOptions(@RequestBody Map<String, String> options) {
        optionService.setOptions(options);
        return Result.success("批量设置成功", null);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "删除配置", description = "删除指定配置项")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteOption(@PathVariable String name) {
        optionService.deleteOption(name);
        return Result.success("删除成功", null);
    }
}
