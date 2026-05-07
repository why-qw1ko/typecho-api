package com.typecho.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.ContentDTO;
import com.typecho.entity.Content;
import com.typecho.entity.Meta;
import com.typecho.entity.Relationship;
import com.typecho.entity.User;
import com.typecho.mapper.ContentMapper;
import com.typecho.mapper.MetaMapper;
import com.typecho.mapper.RelationshipMapper;
import com.typecho.mapper.UserMapper;
import com.typecho.security.LoginUser;
import com.typecho.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentMapper contentMapper;
    private final MetaMapper metaMapper;
    private final RelationshipMapper relationshipMapper;
    private final UserMapper userMapper;

    @Override
    public Content getById(Long cid) {
        return contentMapper.selectById(cid);
    }

    @Override
    public Content getBySlug(String slug) {
        return contentMapper.selectOne(
                new LambdaQueryWrapper<Content>().eq(Content::getSlug, slug)
        );
    }

    @Override
    public Page<Content> page(int pageNum, int pageSize, String type, String status, String keyword, Long authorId) {
        LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Content::getType, type);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Content::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Content::getTitle, keyword);
        }
        if (authorId != null) {
            wrapper.eq(Content::getAuthorId, authorId);
        }
        wrapper.orderByDesc(Content::getCreated);
        return contentMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<Content> listByAuthor(Long authorId) {
        return contentMapper.selectList(
                new LambdaQueryWrapper<Content>()
                        .eq(Content::getAuthorId, authorId)
                        .eq(Content::getStatus, "publish")
                        .orderByDesc(Content::getCreated)
        );
    }

    @Override
    public List<Content> listByCategory(Long mid) {
        List<Relationship> relationships = relationshipMapper.selectList(
                new LambdaQueryWrapper<Relationship>().eq(Relationship::getMid, mid)
        );
        if (relationships.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> cids = relationships.stream().map(Relationship::getCid).toList();
        return contentMapper.selectBatchIds(cids);
    }

    @Override
    public List<Content> listByTag(Long mid) {
        return listByCategory(mid);
    }

    @Override
    @Transactional
    public Long create(ContentDTO dto) {
        Content content = new Content();
        content.setTitle(dto.getTitle());
        content.setSlug(dto.getSlug());
        content.setText(dto.getText());
        content.setType(dto.getType() != null ? dto.getType() : "post");
        content.setStatus(dto.getStatus() != null ? dto.getStatus() : "publish");
        content.setPassword(dto.getPassword());
        content.setOrder(dto.getOrder() != null ? dto.getOrder() : 0);
        content.setTemplate(dto.getTemplate());
        content.setAllowComment(dto.getAllowComment() != null ? dto.getAllowComment() : "1");
        content.setAllowPing(dto.getAllowPing() != null ? dto.getAllowPing() : "1");
        content.setAllowFeed(dto.getAllowFeed() != null ? dto.getAllowFeed() : "1");
        content.setParent(dto.getParent() != null ? dto.getParent() : 0);
        content.setCommentsNum(0);
        content.setViews(0);

        long timestamp = System.currentTimeMillis() / 1000;
        content.setCreated(timestamp);
        content.setModified(timestamp);

        // 获取当前用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof LoginUser loginUser) {
            content.setAuthorId(loginUser.getUser().getUid());
        }

        contentMapper.insert(content);

        // 处理分类和标签关联
        if (dto.getCategoryId() != null) {
            Relationship relationship = new Relationship();
            relationship.setCid(content.getCid());
            relationship.setMid(dto.getCategoryId());
            relationshipMapper.insert(relationship);
        }

        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            for (Long tagId : dto.getTagIds()) {
                Relationship relationship = new Relationship();
                relationship.setCid(content.getCid());
                relationship.setMid(tagId);
                relationshipMapper.insert(relationship);
            }
        }

        return content.getCid();
    }

    @Override
    @Transactional
    public void update(ContentDTO dto) {
        Content content = contentMapper.selectById(dto.getCid());
        if (content == null) {
            throw new RuntimeException("内容不存在");
        }

        if (dto.getTitle() != null) content.setTitle(dto.getTitle());
        if (dto.getSlug() != null) content.setSlug(dto.getSlug());
        if (dto.getText() != null) content.setText(dto.getText());
        if (dto.getStatus() != null) content.setStatus(dto.getStatus());
        if (dto.getPassword() != null) content.setPassword(dto.getPassword());
        if (dto.getOrder() != null) content.setOrder(dto.getOrder());
        if (dto.getTemplate() != null) content.setTemplate(dto.getTemplate());
        if (dto.getAllowComment() != null) content.setAllowComment(dto.getAllowComment());
        if (dto.getAllowPing() != null) content.setAllowPing(dto.getAllowPing());
        if (dto.getAllowFeed() != null) content.setAllowFeed(dto.getAllowFeed());

        content.setModified(System.currentTimeMillis() / 1000);
        contentMapper.updateById(content);

        // 更新分类和标签关联
        if (dto.getCategoryId() != null || (dto.getTagIds() != null && !dto.getTagIds().isEmpty())) {
            // 删除旧的关联
            relationshipMapper.delete(
                    new LambdaQueryWrapper<Relationship>().eq(Relationship::getCid, dto.getCid())
            );

            // 添加新的关联
            if (dto.getCategoryId() != null) {
                Relationship relationship = new Relationship();
                relationship.setCid(dto.getCid());
                relationship.setMid(dto.getCategoryId());
                relationshipMapper.insert(relationship);
            }

            if (dto.getTagIds() != null) {
                for (Long tagId : dto.getTagIds()) {
                    Relationship relationship = new Relationship();
                    relationship.setCid(dto.getCid());
                    relationship.setMid(tagId);
                    relationshipMapper.insert(relationship);
                }
            }
        }
    }

    @Override
    public void delete(Long cid) {
        contentMapper.deleteById(cid);
        relationshipMapper.delete(
                new LambdaQueryWrapper<Relationship>().eq(Relationship::getCid, cid)
        );
    }

    @Override
    public void batchDelete(List<Long> cids) {
        contentMapper.deleteBatchIds(cids);
        for (Long cid : cids) {
            relationshipMapper.delete(
                    new LambdaQueryWrapper<Relationship>().eq(Relationship::getCid, cid)
            );
        }
    }

    @Override
    public void incrementViews(Long cid) {
        Content content = contentMapper.selectById(cid);
        if (content != null) {
            content.setViews(content.getViews() + 1);
            contentMapper.updateById(content);
        }
    }

    @Override
    public Map<String, Object> getDetailWithMeta(Long cid) {
        Content content = contentMapper.selectById(cid);
        if (content == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);

        // 获取作者信息
        User author = userMapper.selectById(content.getAuthorId());
        if (author != null) {
            Map<String, Object> authorInfo = new HashMap<>();
            authorInfo.put("uid", author.getUid());
            authorInfo.put("name", author.getName());
            authorInfo.put("screenName", author.getScreenName());
            authorInfo.put("url", author.getUrl());
            result.put("author", authorInfo);
        }

        // 获取分类和标签
        List<Relationship> relationships = relationshipMapper.selectList(
                new LambdaQueryWrapper<Relationship>().eq(Relationship::getCid, cid)
        );
        if (!relationships.isEmpty()) {
            List<Long> mids = relationships.stream().map(Relationship::getMid).toList();
            List<Meta> metas = metaMapper.selectBatchIds(mids);
            result.put("category", metas.stream().filter(m -> "category".equals(m.getType())).findFirst().orElse(null));
            result.put("tags", metas.stream().filter(m -> "tag".equals(m.getType())).toList());
        }

        return result;
    }

    @Override
    public Page<Map<String, Object>> pageWithAuthor(int pageNum, int pageSize, String type, String status, String keyword) {
        Page<Content> contentPage = page(pageNum, pageSize, type, status, keyword, null);

        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize, contentPage.getTotal());
        List<Map<String, Object>> records = new ArrayList<>();

        for (Content content : contentPage.getRecords()) {
            Map<String, Object> item = new HashMap<>();
            item.put("content", content);

            User author = userMapper.selectById(content.getAuthorId());
            if (author != null) {
                Map<String, Object> authorInfo = new HashMap<>();
                authorInfo.put("uid", author.getUid());
                authorInfo.put("name", author.getName());
                authorInfo.put("screenName", author.getScreenName());
                item.put("author", authorInfo);
            }

            records.add(item);
        }

        resultPage.setRecords(records);
        return resultPage;
    }
}
