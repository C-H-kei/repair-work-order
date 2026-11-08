package com.dormitory.repairbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dormitory.repairbackend.mapper") // 强制扫描 mapper 包
public class RepairBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepairBackendApplication.class, args);
    }
}