package com.typecho.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.UserUpdateDTO;
import com.typecho.entity.User;
import com.typecho.mapper.UserMapper;
import com.typecho.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(Long uid) {
        return userMapper.selectById(uid);
    }

    @Override
    public User getByName(String name) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getName, name)
        );
    }

    @Override
    public User getByMail(String mail) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getMail, mail)
        );
    }

    @Override
    public List<User> listAll() {
        return userMapper.selectList(null);
    }

    @Override
    public Page<User> page(int pageNum, int pageSize, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getName, keyword)
                    .or().like(User::getMail, keyword)
                    .or().like(User::getScreenName, keyword);
        }
        wrapper.orderByDesc(User::getUid);
        return userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Long create(User user) {
        user.setCreated(System.currentTimeMillis() / 1000);
        user.setActivated(System.currentTimeMillis() / 1000);
        if (user.getPassword() != null && !user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.insert(user);
        return user.getUid();
    }

    @Override
    public void update(User user) {
        user.setLogged(System.currentTimeMillis() / 1000);
        userMapper.updateById(user);
    }

    @Override
    public void updateProfile(UserUpdateDTO dto) {
        User user = new User();
        user.setUid(dto.getUid());
        user.setMail(dto.getMail());
        user.setUrl(dto.getUrl());
        user.setScreenName(dto.getScreenName());
        if (dto.getGroup() != null) {
            user.setGroup(dto.getGroup());
        }
        if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }
        userMapper.updateById(user);
    }

    @Override
    public void delete(Long uid) {
        userMapper.deleteById(uid);
    }

    @Override
    public void batchDelete(List<Long> uids) {
        userMapper.deleteBatchIds(uids);
    }
}
