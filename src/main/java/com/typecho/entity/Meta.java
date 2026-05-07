package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_metas")
public class Meta implements Serializable {
    @TableId(value = "mid", type = IdType.AUTO)
    private Long mid;

    private String name;

    private String slug;

    private String type;

    private String description;

    private Integer count;

    @TableField(value = "`order`")
    private Integer order;

    private Integer parent;
}
