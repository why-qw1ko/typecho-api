package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_contents")
public class Content implements Serializable {
    @TableId(value = "cid", type = IdType.AUTO)
    private Long cid;

    private String title;

    private String slug;

    private Long created;

    private Long modified;

    private String text;

    @TableField(value = "`order`")
    private Integer order;

    @TableField("authorId")
    private Long authorId;

    private String template;

    private String type;

    private String status;

    private String password;

    @TableField(value = "commentsNum")
    private Integer commentsNum;

    @TableField("allowComment")
    private String allowComment;

    @TableField("allowPing")
    private String allowPing;

    @TableField("allowFeed")
    private String allowFeed;

    private Integer parent;

    private Integer views;
}
