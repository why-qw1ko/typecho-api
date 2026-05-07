package com.typecho.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.typecho.entity.Option;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionMapper extends BaseMapper<Option> {
}
