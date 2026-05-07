package com.typecho.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.typecho.entity.Relationship;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RelationshipMapper extends BaseMapper<Relationship> {
}
