package com.typecho.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.LinkDTO;
import com.typecho.entity.Link;

import java.util.List;

public interface LinkService {
    Link getById(Long lid);

    List<Link> listAll();

    List<Link> listBySort(String sort);

    Page<Link> page(int pageNum, int pageSize, String keyword);

    Long create(LinkDTO dto);

    void update(LinkDTO dto);

    void delete(Long lid);

    void batchDelete(List<Long> lids);
}
