package com.typecho.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.typecho.dto.CommentDTO;
import com.typecho.entity.Comment;
import com.typecho.entity.Content;
import com.typecho.entity.User;
import com.typecho.mapper.CommentMapper;
import com.typecho.mapper.ContentMapper;
import com.typecho.mapper.UserMapper;
import com.typecho.security.LoginUser;
import com.typecho.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final ContentMapper contentMapper;
    private final UserMapper userMapper;

    @Override
    public Comment getById(Long coid) {
        return commentMapper.selectById(coid);
    }

    @Override
    public Page<Comment> page(int pageNum, int pageSize, Long cid, String status, String keyword) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (cid != null) {
            wrapper.eq(Comment::getCid, cid);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Comment::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Comment::getText, keyword);
        }
        wrapper.orderByDesc(Comment::getCreated);
        return commentMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Page<Comment> pageByContent(Long cid, int pageNum, int pageSize) {
        return commentMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getCid, cid)
                        .eq(Comment::getStatus, "approved")
                        .orderByDesc(Comment::getCreated)
        );
    }

    @Override
    public List<Comment> listByContent(Long cid) {
        return commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getCid, cid)
                        .eq(Comment::getStatus, "approved")
                        .orderByDesc(Comment::getCreated)
        );
    }

    @Override
    public Long create(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setCid(dto.getCid());
        comment.setText(dto.getText());
        comment.setParent(dto.getParent() != null ? dto.getParent() : 0L);
        comment.setStatus(dto.getStatus() != null ? dto.getStatus() : "waiting");
        comment.setStars(dto.getStars() != null ? dto.getStars() : 0);
        comment.setCreated(System.currentTimeMillis() / 1000);
        comment.setType("comment");

        // 获取当前用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof LoginUser loginUser) {
            User user = loginUser.getUser();
            comment.setAuthorId(user.getUid());
            comment.setAuthor(user.getScreenName() != null ? user.getScreenName() : user.getName());
            comment.setMail(user.getMail());
            comment.setUrl(user.getUrl());

            // 获取文章作者ID
            Content content = contentMapper.selectById(dto.getCid());
            if (content != null) {
                comment.setOwnerId(content.getAuthorId());
            }
        } else {
            // 游客评论
            comment.setAuthorId(0L);
            comment.setAuthor(dto.getAuthor());
            comment.setMail(dto.getMail());
            comment.setUrl(dto.getUrl());
        }

        commentMapper.insert(comment);

        // 更新文章评论数
        if ("approved".equals(comment.getStatus())) {
            updateContentCommentsNum(dto.getCid(), 1);
        }

        return comment.getCoid();
    }

    @Override
    public void update(CommentDTO dto) {
        Comment comment = commentMapper.selectById(dto.getCoid());
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        if (dto.getText() != null) comment.setText(dto.getText());
        if (dto.getStatus() != null) comment.setStatus(dto.getStatus());
        if (dto.getStars() != null) comment.setStars(dto.getStars());

        commentMapper.updateById(comment);
    }

    @Override
    public void delete(Long coid) {
        Comment comment = commentMapper.selectById(coid);
        if (comment != null) {
            commentMapper.deleteById(coid);
            // 更新文章评论数
            if ("approved".equals(comment.getStatus())) {
                updateContentCommentsNum(comment.getCid(), -1);
            }
        }
    }

    @Override
    public void batchDelete(List<Long> coids) {
        for (Long coid : coids) {
            delete(coid);
        }
    }

    @Override
    public void approve(Long coid) {
        Comment comment = commentMapper.selectById(coid);
        if (comment != null && !"approved".equals(comment.getStatus())) {
            comment.setStatus("approved");
            commentMapper.updateById(comment);
            updateContentCommentsNum(comment.getCid(), 1);
        }
    }

    @Override
    public void reject(Long coid) {
        Comment comment = commentMapper.selectById(coid);
        if (comment != null && "approved".equals(comment.getStatus())) {
            comment.setStatus("waiting");
            commentMapper.updateById(comment);
            updateContentCommentsNum(comment.getCid(), -1);
        }
    }

    @Override
    public void markAsSpam(Long coid) {
        Comment comment = commentMapper.selectById(coid);
        if (comment != null) {
            if ("approved".equals(comment.getStatus())) {
                updateContentCommentsNum(comment.getCid(), -1);
            }
            comment.setStatus("spam");
            commentMapper.updateById(comment);
        }
    }

    @Override
    public Page<Map<String, Object>> pageWithContent(int pageNum, int pageSize, String status, String keyword) {
        Page<Comment> commentPage = page(pageNum, pageSize, null, status, keyword);

        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize, commentPage.getTotal());
        List<Map<String, Object>> records = new ArrayList<>();

        for (Comment comment : commentPage.getRecords()) {
            Map<String, Object> item = new HashMap<>();
            item.put("comment", comment);

            Content content = contentMapper.selectById(comment.getCid());
            if (content != null) {
                Map<String, Object> contentInfo = new HashMap<>();
                contentInfo.put("cid", content.getCid());
                contentInfo.put("title", content.getTitle());
                item.put("content", contentInfo);
            }

            records.add(item);
        }

        resultPage.setRecords(records);
        return resultPage;
    }

    @Override
    public Long countByContent(Long cid) {
        return commentMapper.selectCount(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getCid, cid)
                        .eq(Comment::getStatus, "approved")
        );
    }

    private void updateContentCommentsNum(Long cid, int delta) {
        Content content = contentMapper.selectById(cid);
        if (content != null) {
            content.setCommentsNum(Math.max(0, content.getCommentsNum() + delta));
            contentMapper.updateById(content);
        }
    }
}
