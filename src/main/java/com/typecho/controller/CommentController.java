package com.typecho.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.common.PageResult;
import com.typecho.common.Result;
import com.typecho.dto.CommentDTO;
import com.typecho.entity.Comment;
import com.typecho.service.CommentService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "评论管理", description = "评论相关接口")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{coid}")
    @Operation(summary = "获取评论详情", description = "根据ID获取评论详情")
    public Result<Comment> getById(@PathVariable Long coid) {
        Comment comment = commentService.getById(coid);
        return Result.success(comment);
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取评论", description = "分页获取评论列表（管理后台）")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Page<Map<String, Object>> page = commentService.pageWithContent(pageNum, pageSize, status, keyword);
        return Result.success(PageResult.of(page));
    }

    @GetMapping("/content/{cid}")
    @Operation(summary = "获取文章的评论", description = "获取指定文章的评论列表")
    public Result<PageResult<Comment>> pageByContent(
            @PathVariable Long cid,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize) {
        Page<Comment> page = commentService.pageByContent(cid, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }

    @GetMapping("/count/{cid}")
    @Operation(summary = "获取文章评论数", description = "获取指定文章的评论数量")
    public Result<Long> countByContent(@PathVariable Long cid) {
        Long count = commentService.countByContent(cid);
        return Result.success(count);
    }

    @PostMapping
    @Operation(summary = "发表评论", description = "发表新评论")
    public Result<Long> create(@Valid @RequestBody CommentDTO dto) {
        Long coid = commentService.create(dto);
        return Result.success("评论成功", coid);
    }

    @PutMapping
    @Operation(summary = "更新评论", description = "更新评论内容")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@Valid @RequestBody CommentDTO dto) {
        commentService.update(dto);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{coid}")
    @Operation(summary = "删除评论", description = "删除评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long coid) {
        commentService.delete(coid);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除评论", description = "批量删除评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> coids) {
        commentService.batchDelete(coids);
        return Result.success("批量删除成功", null);
    }

    @PutMapping("/{coid}/approve")
    @Operation(summary = "审核通过", description = "审核通过评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> approve(@PathVariable Long coid) {
        commentService.approve(coid);
        return Result.success("审核通过", null);
    }

    @PutMapping("/{coid}/reject")
    @Operation(summary = "审核拒绝", description = "拒绝评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> reject(@PathVariable Long coid) {
        commentService.reject(coid);
        return Result.success("已拒绝", null);
    }

    @PutMapping("/{coid}/spam")
    @Operation(summary = "标记为垃圾", description = "将评论标记为垃圾评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> markAsSpam(@PathVariable Long coid) {
        commentService.markAsSpam(coid);
        return Result.success("已标记为垃圾", null);
    }
}
