package com.huibai.eomp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@MapperScan("com.huibai.eomp.mapper")
@SpringBootApplication
@EnableScheduling
public class EompApplication {

    public static void main(String[] args) {
        SpringApplication.run(EompApplication.class, args);
    }

    @Component
    public class AttendanceTask {
        @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点执行
        public void checkAbsent() {
            // 逻辑：获取所有员工列表 -> 检查昨天是否有记录 -> 没有则插入一条 STATUS='ABSENT'
            System.out.println("自动扫描昨日缺勤人员...");
        }
    }
}
