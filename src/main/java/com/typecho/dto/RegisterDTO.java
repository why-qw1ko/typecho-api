package com.typecho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "注册请求")
public class RegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "testuser")
    private String name;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "test@example.com")
    private String mail;

    @Schema(description = "个人网址", example = "https://example.com")
    private String url;

    @Schema(description = "昵称", example = "测试用户")
    private String screenName;
}
