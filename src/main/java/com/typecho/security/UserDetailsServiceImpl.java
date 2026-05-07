package com.typecho.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.typecho.entity.User;
import com.typecho.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 尝试作为用户ID解析
        try {
            Long uid = Long.parseLong(username);
            User user = userMapper.selectById(uid);
            if (user != null) {
                return new LoginUser(user);
            }
        } catch (NumberFormatException ignored) {
        }

        // 作为用户名解析
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getName, username)
        );

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        return new LoginUser(user);
    }
}
