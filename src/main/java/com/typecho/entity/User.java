package com.typecho.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("typecho_users")
public class User implements Serializable {
    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    private String name;

    private String password;

    private String mail;

    private String url;

    @TableField("screenName")
    private String screenName;

    private Long created;

    private Long activated;

    private Long logged;

    @TableField(value = "`group`")
    private String group;

    @TableField(exist = false)
    private String authCode;
}
