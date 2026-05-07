package com.typecho;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.typecho.mapper")
public class TypechoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TypechoApplication.class, args);
    }
}
