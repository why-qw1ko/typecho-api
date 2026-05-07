package com.typecho.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.LinkDTO;
import com.typecho.entity.Link;
import com.typecho.mapper.LinkMapper;
import com.typecho.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkMapper linkMapper;

    @Override
    public Link getById(Long lid) {
        return linkMapper.selectById(lid);
    }

    @Override
    public List<Link> listAll() {
        return linkMapper.selectList(
                new LambdaQueryWrapper<Link>().orderByAsc(Link::getOrder)
        );
    }

    @Override
    public List<Link> listBySort(String sort) {
        return linkMapper.selectList(
                new LambdaQueryWrapper<Link>()
                        .eq(Link::getSort, sort)
                        .orderByAsc(Link::getOrder)
        );
    }

    @Override
    public Page<Link> page(int pageNum, int pageSize, String keyword) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Link::getName, keyword)
                    .or().like(Link::getUrl, keyword);
        }
        wrapper.orderByAsc(Link::getOrder);
        return linkMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Long create(LinkDTO dto) {
        Link link = new Link();
        link.setName(dto.getName());
        link.setUrl(dto.getUrl());
        link.setSort(dto.getSort());
        link.setImage(dto.getImage());
        link.setDescription(dto.getDescription());
        link.setOrder(dto.getOrder() != null ? dto.getOrder() : 0);
        linkMapper.insert(link);
        return link.getLid();
    }

    @Override
    public void update(LinkDTO dto) {
        Link link = linkMapper.selectById(dto.getLid());
        if (link == null) {
            throw new RuntimeException("链接不存在");
        }
        if (dto.getName() != null) link.setName(dto.getName());
        if (dto.getUrl() != null) link.setUrl(dto.getUrl());
        if (dto.getSort() != null) link.setSort(dto.getSort());
        if (dto.getImage() != null) link.setImage(dto.getImage());
        if (dto.getDescription() != null) link.setDescription(dto.getDescription());
        if (dto.getOrder() != null) link.setOrder(dto.getOrder());
        linkMapper.updateById(link);
    }

    @Override
    public void delete(Long lid) {
        linkMapper.deleteById(lid);
    }

    @Override
    public void batchDelete(List<Long> lids) {
        linkMapper.deleteBatchIds(lids);
    }
}
