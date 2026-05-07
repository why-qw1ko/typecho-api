package com.typecho.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.typecho.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
