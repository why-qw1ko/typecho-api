package com.typecho.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.ContentDTO;
import com.typecho.entity.Content;

import java.util.List;
import java.util.Map;

public interface ContentService {
    Content getById(Long cid);

    Content getBySlug(String slug);

    Page<Content> page(int pageNum, int pageSize, String type, String status, String keyword, Long authorId);

    List<Content> listByAuthor(Long authorId);

    List<Content> listByCategory(Long mid);

    List<Content> listByTag(Long mid);

    Long create(ContentDTO dto);

    void update(ContentDTO dto);

    void delete(Long cid);

    void batchDelete(List<Long> cids);

    void incrementViews(Long cid);

    Map<String, Object> getDetailWithMeta(Long cid);

    Page<Map<String, Object>> pageWithAuthor(int pageNum, int pageSize, String type, String status, String keyword);
}
