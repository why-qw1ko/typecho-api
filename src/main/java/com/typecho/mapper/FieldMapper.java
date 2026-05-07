package com.typecho.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.typecho.entity.Field;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FieldMapper extends BaseMapper<Field> {
}
