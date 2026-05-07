package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_comments")
public class Comment implements Serializable {
    @TableId(value = "coid", type = IdType.AUTO)
    private Long coid;

    private Long cid;

    private Long created;

    private String author;

    @TableField("authorId")
    private Long authorId;

    @TableField("ownerId")
    private Long ownerId;

    private String mail;

    private String url;

    private String ip;

    private String agent;

    private String text;

    private String type;

    private String status;

    private Long parent;

    private Integer stars;
}
