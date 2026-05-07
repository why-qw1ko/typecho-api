package com.typecho.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.common.BusinessException;
import com.typecho.dto.LoginDTO;
import com.typecho.dto.RegisterDTO;
import com.typecho.entity.User;
import com.typecho.mapper.UserMapper;
import com.typecho.security.JwtTokenProvider;
import com.typecho.security.LoginUser;
import com.typecho.service.AuthService;
import com.typecho.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getName, dto.getUsername())
        );

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // Typecho 使用 md5 加密密码，需要特殊处理
        // 这里先检查是否是 bcrypt 加密的密码
        if (user.getPassword().startsWith("$2")) {
            // bcrypt 密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            return jwtTokenProvider.generateToken(authentication);
        } else {
            // Typecho MD5 密码验证 (MD5密码暂时失效。)
            String md5Password = cn.hutool.crypto.digest.DigestUtil.md5Hex(dto.getPassword());
            if (!md5Password.equals(user.getPassword())) {
                throw new BusinessException("用户名或密码错误");
            }

            LoginUser loginUser = new LoginUser(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginUser, null, loginUser.getAuthorities()
            );
            return jwtTokenProvider.generateToken(authentication);
        }
    }

    @Override
    public Long register(RegisterDTO dto) {
        // 检查用户名是否存在
        if (userService.getByName(dto.getName()) != null) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否存在
        if (userService.getByMail(dto.getMail()) != null) {
            throw new BusinessException("邮箱已被注册");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setMail(dto.getMail());
        user.setUrl(dto.getUrl());
        user.setScreenName(dto.getScreenName() != null ? dto.getScreenName() : dto.getName());
        user.setCreated(System.currentTimeMillis() / 1000);
        user.setActivated(System.currentTimeMillis() / 1000);
        user.setGroup("subscriber");

        userService.create(user);
        return user.getUid();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(401, "未登录");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser();
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        User currentUser = getCurrentUser();

        // 验证旧密码
        if (currentUser.getPassword().startsWith("$2")) {
            if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                throw new BusinessException("旧密码错误");
            }
        } else {
            String md5OldPassword = cn.hutool.crypto.digest.DigestUtil.md5Hex(oldPassword);
            if (!md5OldPassword.equals(currentUser.getPassword())) {
                throw new BusinessException("旧密码错误");
            }
        }

        // 更新密码
        User updateUser = new User();
        updateUser.setUid(currentUser.getUid());
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        userService.update(updateUser);
    }
}
