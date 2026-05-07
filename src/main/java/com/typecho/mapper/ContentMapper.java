package com.typecho.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.typecho.entity.Content;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentMapper extends BaseMapper<Content> {
}
