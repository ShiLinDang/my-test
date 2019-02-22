package com.my.test.demo.runner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:04
 */
@SpringBootApplication
@MapperScan("com.my.test.demo.dao")// 扫描mapper映射实体
@ComponentScan(basePackages = {"com.my.test.demo.*"})
public class SysUserApp {
    public static void main(String[] args) {
        SpringApplication.run(SysUserApp.class,args);
    }
}
