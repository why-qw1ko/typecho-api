package com.typecho.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessException extends RuntimeException implements Serializable {
    private Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
