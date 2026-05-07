package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_relationships")
public class Relationship implements Serializable {
    private Long cid;

    private Long mid;
}
