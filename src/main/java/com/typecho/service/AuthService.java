package com.typecho.service;

import com.typecho.dto.LoginDTO;
import com.typecho.dto.RegisterDTO;
import com.typecho.entity.User;

public interface AuthService {
    String login(LoginDTO dto);

    Long register(RegisterDTO dto);

    User getCurrentUser();

    void updatePassword(String oldPassword, String newPassword);
}
