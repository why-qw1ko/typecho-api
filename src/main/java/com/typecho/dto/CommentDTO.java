package com.typecho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "评论DTO")
public class CommentDTO {
    @Schema(description = "评论ID（更新时需要）")
    private Long coid;

    @Schema(description = "文章ID")
    private Long cid;

    @NotBlank(message = "评论内容不能为空")
    @Schema(description = "评论内容", example = "这是一条评论")
    private String text;

    @Schema(description = "评论者名称（游客评论时需要）")
    private String author;

    @Schema(description = "评论者邮箱（游客评论时需要）")
    @Email(message = "邮箱格式不正确")
    private String mail;

    @Schema(description = "评论者网址")
    private String url;

    @Schema(description = "父评论ID")
    private Long parent;

    @Schema(description = "状态：approved-已审核，waiting-待审核，spam-垃圾")
    private String status;

    @Schema(description = "评分")
    private Integer stars;
}
