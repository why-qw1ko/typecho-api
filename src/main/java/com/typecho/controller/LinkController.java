package com.typecho.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.common.PageResult;
import com.typecho.common.Result;
import com.typecho.dto.LinkDTO;
import com.typecho.entity.Link;
import com.typecho.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@Tag(name = "友情链接管理", description = "友情链接相关接口")
public class LinkController {

    private final LinkService linkService;

    @GetMapping("/{lid}")
    @Operation(summary = "获取链接详情", description = "根据ID获取链接详情")
    public Result<Link> getById(@PathVariable Long lid) {
        Link link = linkService.getById(lid);
        return Result.success(link);
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有链接", description = "获取所有友情链接列表")
    public Result<List<Link>> listAll() {
        List<Link> links = linkService.listAll();
        return Result.success(links);
    }

    @GetMapping("/sort/{sort}")
    @Operation(summary = "按分类获取链接", description = "获取指定分类的链接")
    public Result<List<Link>> listBySort(@PathVariable String sort) {
        List<Link> links = linkService.listBySort(sort);
        return Result.success(links);
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取链接", description = "分页获取链接列表")
    public Result<PageResult<Link>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Page<Link> page = linkService.page(pageNum, pageSize, keyword);
        return Result.success(PageResult.of(page));
    }

    @PostMapping
    @Operation(summary = "创建链接", description = "创建新的友情链接")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> create(@Valid @RequestBody LinkDTO dto) {
        Long lid = linkService.create(dto);
        return Result.success("创建成功", lid);
    }

    @PutMapping
    @Operation(summary = "更新链接", description = "更新友情链接")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@Valid @RequestBody LinkDTO dto) {
        linkService.update(dto);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{lid}")
    @Operation(summary = "删除链接", description = "删除友情链接")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long lid) {
        linkService.delete(lid);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除", description = "批量删除链接")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> lids) {
        linkService.batchDelete(lids);
        return Result.success("批量删除成功", null);
    }
}
