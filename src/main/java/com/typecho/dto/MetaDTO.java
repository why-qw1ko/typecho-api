package com.typecho.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "分类/标签DTO")
public class MetaDTO {
    @Schema(description = "ID（更新时需要）")
    private Long mid;

    @NotBlank(message = "名称不能为空")
    @Schema(description = "名称", example = "技术分享")
    private String name;

    @NotBlank(message = "别名不能为空")
    @Schema(description = "别名", example = "tech")
    private String slug;

    @NotBlank(message = "类型不能为空")
    @Schema(description = "类型：category-分类，tag-标签", example = "category")
    private String type;

    @Schema(description = "描述", example = "技术相关文章")
    private String description;

    @Schema(description = "排序")
    @TableField(value = "`order`")
    private Integer order;

    @Schema(description = "父级ID")
    private Integer parent;
}
