package com.cjt.demo.springaopdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.cjt.demo.springaopdemo.mapper")
public class SpringAopDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAopDemoApplication.class, args);
    }

}
