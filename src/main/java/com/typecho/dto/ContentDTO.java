package com.typecho.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "内容文章DTO")
public class ContentDTO {
    @Schema(description = "文章ID（更新时需要）")
    private Long cid;

    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题", example = "我的第一篇文章")
    private String title;

    @NotBlank(message = "别名不能为空")
    @Schema(description = "别名（URL友好）", example = "my-first-post")
    private String slug;

    @Schema(description = "内容", example = "这是文章正文...")
    private String text;

    @Schema(description = "类型：post-文章，page-页面", example = "post")
    private String type;

    @Schema(description = "状态：publish-发布，draft-草稿，hidden-隐藏", example = "publish")
    private String status;

    @Schema(description = "密码保护")
    private String password;

    @Schema(description = "排序")
    @TableField(value = "`order`")
    private Integer order;

    @Schema(description = "模板")
    private String template;

    @Schema(description = "是否允许评论", example = "1")
    private String allowComment;

    @Schema(description = "是否允许引用", example = "1")
    private String allowPing;

    @Schema(description = "是否允许订阅", example = "1")
    private String allowFeed;

    @Schema(description = "父级ID")
    private Integer parent;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "标签ID列表")
    private java.util.List<Long> tagIds;
}
