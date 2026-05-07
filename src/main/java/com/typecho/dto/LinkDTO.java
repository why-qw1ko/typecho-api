package com.typecho.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "友情链接DTO")
public class LinkDTO {
    @Schema(description = "链接ID（更新时需要）")
    private Long lid;

    @NotBlank(message = "链接名称不能为空")
    @Schema(description = "链接名称", example = "Typecho官网")
    private String name;

    @NotBlank(message = "链接地址不能为空")
    @Schema(description = "链接地址", example = "https://typecho.org")
    private String url;

    @Schema(description = "分类", example = "友情链接")
    private String sort;

    @Schema(description = "图片地址", example = "https://typecho.org/logo.png")
    private String image;

    @Schema(description = "描述", example = "Typecho开源博客系统")
    private String description;

    @Schema(description = "排序")
    @TableField(value = "`order`")
    private Integer order;
}
