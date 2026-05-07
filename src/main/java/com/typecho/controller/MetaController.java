package com.typecho.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.common.PageResult;
import com.typecho.common.Result;
import com.typecho.dto.MetaDTO;
import com.typecho.entity.Meta;
import com.typecho.service.MetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
@RequiredArgsConstructor
@Tag(name = "分类/标签管理", description = "分类和标签相关接口")
public class MetaController {

    private final MetaService metaService;

    @GetMapping("/{mid}")
    @Operation(summary = "获取分类/标签详情", description = "根据ID获取分类或标签详情")
    public Result<Meta> getById(@PathVariable Long mid) {
        Meta meta = metaService.getById(mid);
        return Result.success(meta);
    }

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类", description = "获取所有分类列表")
    public Result<List<Meta>> listCategories() {
        List<Meta> categories = metaService.listCategories();
        return Result.success(categories);
    }

    @GetMapping("/tags")
    @Operation(summary = "获取所有标签", description = "获取所有标签列表")
    public Result<List<Meta>> listTags() {
        List<Meta> tags = metaService.listTags();
        return Result.success(tags);
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取分类/标签", description = "分页获取分类或标签列表")
    public Result<PageResult<Meta>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "类型：category-分类，tag-标签") @RequestParam(required = false) String type,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Page<Meta> page = metaService.page(pageNum, pageSize, type, keyword);
        return Result.success(PageResult.of(page));
    }

    @PostMapping
    @Operation(summary = "创建分类/标签", description = "创建新的分类或标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> create(@Valid @RequestBody MetaDTO dto) {
        Long mid = metaService.create(dto);
        return Result.success("创建成功", mid);
    }

    @PutMapping
    @Operation(summary = "更新分类/标签", description = "更新分类或标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@Valid @RequestBody MetaDTO dto) {
        metaService.update(dto);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{mid}")
    @Operation(summary = "删除分类/标签", description = "删除分类或标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long mid) {
        metaService.delete(mid);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除", description = "批量删除分类或标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> mids) {
        metaService.batchDelete(mids);
        return Result.success("批量删除成功", null);
    }

    @GetMapping("/count")
    @Operation(summary = "统计数量", description = "统计分类或标签数量")
    public Result<Long> count(@Parameter(description = "类型") @RequestParam(required = false) String type) {
        Long count = metaService.countByType(type);
        return Result.success(count);
    }
}
