package com.typecho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户更新DTO")
public class UserUpdateDTO {
    @Schema(description = "用户ID")
    private Long uid;

    @Schema(description = "邮箱")
    private String mail;

    @Schema(description = "个人网址")
    private String url;

    @Schema(description = "昵称")
    private String screenName;

    @Schema(description = "用户组：admin-管理员，editor-编辑，contributor-投稿者，subscriber-订阅者")
    private String group;

    @Schema(description = "新密码（如需修改）")
    private String newPassword;
}
