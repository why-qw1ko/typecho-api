package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_fields")
public class Field implements Serializable {
    private Long cid;

    private String name;

    private String type;

    private String strValue;

    private Integer intValue;

    private Float floatValue;
}
