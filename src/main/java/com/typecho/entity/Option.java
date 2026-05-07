package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_options")
public class Option implements Serializable {
    private String name;

    private Integer user;

    private String value;
}
