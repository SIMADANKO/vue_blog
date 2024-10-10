package com.vueblog.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class RegisterDto {
    @NotBlank(message ="昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}

