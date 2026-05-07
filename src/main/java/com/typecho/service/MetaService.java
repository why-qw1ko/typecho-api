package com.typecho.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.MetaDTO;
import com.typecho.entity.Meta;

import java.util.List;

public interface MetaService {
    Meta getById(Long mid);

    Meta getBySlug(String slug, String type);

    List<Meta> listByType(String type);

    List<Meta> listCategories();

    List<Meta> listTags();

    Page<Meta> page(int pageNum, int pageSize, String type, String keyword);

    Long create(MetaDTO dto);

    void update(MetaDTO dto);

    void delete(Long mid);

    void batchDelete(List<Long> mids);

    Long countByType(String type);

    void incrementCount(Long mid);

    void decrementCount(Long mid);
}
