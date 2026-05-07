package com.typecho.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.UserUpdateDTO;
import com.typecho.entity.User;

import java.util.List;

public interface UserService {
    User getById(Long uid);

    User getByName(String name);

    User getByMail(String mail);

    List<User> listAll();

    Page<User> page(int pageNum, int pageSize, String keyword);

    Long create(User user);

    void update(User user);

    void updateProfile(UserUpdateDTO dto);

    void delete(Long uid);

    void batchDelete(List<Long> uids);
}
