package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_links")
public class Link implements Serializable {
    @TableId(value = "lid", type = IdType.AUTO)
    private Long lid;

    private String name;

    private String url;

    private String sort;

    private String image;

    private String description;

    private String user;

    // 解决order和mysql中关键字冲突
    @TableField(value = "`order`")
    private Integer order;
}
