package com.typecho.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.MetaDTO;
import com.typecho.entity.Meta;
import com.typecho.mapper.MetaMapper;
import com.typecho.service.MetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaServiceImpl implements MetaService {

    private final MetaMapper metaMapper;

    @Override
    public Meta getById(Long mid) {
        return metaMapper.selectById(mid);
    }

    @Override
    public Meta getBySlug(String slug, String type) {
        return metaMapper.selectOne(
                new LambdaQueryWrapper<Meta>()
                        .eq(Meta::getSlug, slug)
                        .eq(Meta::getType, type)
        );
    }

    @Override
    public List<Meta> listByType(String type) {
        return metaMapper.selectList(
                new LambdaQueryWrapper<Meta>()
                        .eq(Meta::getType, type)
                        .orderByAsc(Meta::getOrder)
        );
    }

    @Override
    public List<Meta> listCategories() {
        return listByType("category");
    }

    @Override
    public List<Meta> listTags() {
        return listByType("tag");
    }

    @Override
    public Page<Meta> page(int pageNum, int pageSize, String type, String keyword) {
        LambdaQueryWrapper<Meta> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Meta::getType, type);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Meta::getName, keyword);
        }
        wrapper.orderByAsc(Meta::getOrder);
        return metaMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Long create(MetaDTO dto) {
        Meta meta = new Meta();
        meta.setName(dto.getName());
        meta.setSlug(dto.getSlug());
        meta.setType(dto.getType());
        meta.setDescription(dto.getDescription());
        meta.setOrder(dto.getOrder() != null ? dto.getOrder() : 0);
        meta.setParent(dto.getParent() != null ? dto.getParent() : 0);
        meta.setCount(0);
        metaMapper.insert(meta);
        return meta.getMid();
    }

    @Override
    public void update(MetaDTO dto) {
        Meta meta = metaMapper.selectById(dto.getMid());
        if (meta == null) {
            throw new RuntimeException("分类/标签不存在");
        }
        if (dto.getName() != null) meta.setName(dto.getName());
        if (dto.getSlug() != null) meta.setSlug(dto.getSlug());
        if (dto.getDescription() != null) meta.setDescription(dto.getDescription());
        if (dto.getOrder() != null) meta.setOrder(dto.getOrder());
        if (dto.getParent() != null) meta.setParent(dto.getParent());
        metaMapper.updateById(meta);
    }

    @Override
    public void delete(Long mid) {
        metaMapper.deleteById(mid);
    }

    @Override
    public void batchDelete(List<Long> mids) {
        metaMapper.deleteBatchIds(mids);
    }

    @Override
    public Long countByType(String type) {
        return metaMapper.selectCount(
                new LambdaQueryWrapper<Meta>().eq(Meta::getType, type)
        );
    }

    @Override
    public void incrementCount(Long mid) {
        Meta meta = metaMapper.selectById(mid);
        if (meta != null) {
            meta.setCount(meta.getCount() + 1);
            metaMapper.updateById(meta);
        }
    }

    @Override
    public void decrementCount(Long mid) {
        Meta meta = metaMapper.selectById(mid);
        if (meta != null) {
            meta.setCount(Math.max(0, meta.getCount() - 1));
            metaMapper.updateById(meta);
        }
    }
}
