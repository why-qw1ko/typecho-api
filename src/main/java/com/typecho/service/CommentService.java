package com.typecho.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.CommentDTO;
import com.typecho.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {
    Comment getById(Long coid);

    Page<Comment> page(int pageNum, int pageSize, Long cid, String status, String keyword);

    Page<Comment> pageByContent(Long cid, int pageNum, int pageSize);

    List<Comment> listByContent(Long cid);

    Long create(CommentDTO dto);

    void update(CommentDTO dto);

    void delete(Long coid);

    void batchDelete(List<Long> coids);

    void approve(Long coid);

    void reject(Long coid);

    void markAsSpam(Long coid);

    Page<Map<String, Object>> pageWithContent(int pageNum, int pageSize, String status, String keyword);

    Long countByContent(Long cid);
}
