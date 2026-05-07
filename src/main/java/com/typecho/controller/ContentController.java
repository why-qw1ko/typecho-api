package com.typecho.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.common.PageResult;
import com.typecho.common.Result;
import com.typecho.dto.ContentDTO;
import com.typecho.entity.Content;
import com.typecho.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
@Tag(name = "内容管理", description = "文章和页面相关接口")
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/{cid}")
    @Operation(summary = "获取内容详情", description = "根据ID获取文章或页面详情")
    public Result<Map<String, Object>> getById(@PathVariable Long cid) {
        Map<String, Object> detail = contentService.getDetailWithMeta(cid);
        if (detail != null) {
            contentService.incrementViews(cid);
        }
        return Result.success(detail);
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "根据别名获取内容", description = "根据别名获取文章或页面")
    public Result<Content> getBySlug(@PathVariable String slug) {
        Content content = contentService.getBySlug(slug);
        return Result.success(content);
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取内容列表", description = "分页获取文章或页面列表")
    public Result<PageResult<Map<String, Object>>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "类型：post-文章，page-页面") @RequestParam(required = false) String type,
            @Parameter(description = "状态：publish-发布，draft-草稿") @RequestParam(required = false) String status,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Page<Map<String, Object>> page = contentService.pageWithAuthor(pageNum, pageSize, type, status, keyword);
        return Result.success(PageResult.of(page));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "获取作者的文章", description = "获取指定作者的所有文章")
    public Result<List<Content>> listByAuthor(@PathVariable Long authorId) {
        List<Content> contents = contentService.listByAuthor(authorId);
        return Result.success(contents);
    }

    @GetMapping("/category/{mid}")
    @Operation(summary = "获取分类下的文章", description = "获取指定分类下的所有文章")
    public Result<List<Content>> listByCategory(@PathVariable Long mid) {
        List<Content> contents = contentService.listByCategory(mid);
        return Result.success(contents);
    }

    @GetMapping("/tag/{mid}")
    @Operation(summary = "获取标签下的文章", description = "获取指定标签下的所有文章")
    public Result<List<Content>> listByTag(@PathVariable Long mid) {
        List<Content> contents = contentService.listByTag(mid);
        return Result.success(contents);
    }

    @PostMapping
    @Operation(summary = "创建内容", description = "创建新文章或页面")
    @PreAuthorize("isAuthenticated()")
    public Result<Long> create(@Valid @RequestBody ContentDTO dto) {
        Long cid = contentService.create(dto);
        return Result.success("创建成功", cid);
    }

    @PutMapping
    @Operation(summary = "更新内容", description = "更新文章或页面")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> update(@Valid @RequestBody ContentDTO dto) {
        contentService.update(dto);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{cid}")
    @Operation(summary = "删除内容", description = "删除文章或页面（管理员或作者本人可删除）")
    public Result<Void> delete(@PathVariable Long cid) {
        contentService.delete(cid);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除内容", description = "批量删除文章或页面")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> cids) {
        contentService.batchDelete(cids);
        return Result.success("批量删除成功", null);
    }

    @PutMapping("/{cid}/views")
    @Operation(summary = "增加浏览量", description = "增加文章浏览量")
    public Result<Void> incrementViews(@PathVariable Long cid) {
        contentService.incrementViews(cid);
        return Result.success(null);
    }
}
