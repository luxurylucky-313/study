package com.zyt.xxt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.zyt.xxt.mapper")
@EnableCaching
public class XxtApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxtApplication.class, args);
    }
}
