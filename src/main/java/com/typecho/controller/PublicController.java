package com.typecho.controller;

import com.typecho.common.Result;
import com.typecho.service.ContentService;
import com.typecho.service.MetaService;
import com.typecho.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "公开接口", description = "无需认证的公开接口")
public class PublicController {

    private final UserService userService;
    private final ContentService contentService;
    private final MetaService metaService;

    @GetMapping("/stats")
    @Operation(summary = "获取网站统计", description = "获取网站统计数据（公开）")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPosts", contentService.page(1, 1, "post", "publish", null, null).getTotal());
        stats.put("totalPages", contentService.page(1, 1, "page", "publish", null, null).getTotal());
        stats.put("totalCategories", metaService.countByType("category"));
        stats.put("totalTags", metaService.countByType("tag"));
        stats.put("totalUsers", userService.listAll().size());
        return Result.success(stats);
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查服务是否正常")
    public Result<String> health() {
        return Result.success("OK");
    }
}
