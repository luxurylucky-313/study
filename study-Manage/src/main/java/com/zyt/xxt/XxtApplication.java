package com.zyt.xxt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zyt.xxt.mapper")
public class XxtApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxtApplication.class, args);
    }
}
