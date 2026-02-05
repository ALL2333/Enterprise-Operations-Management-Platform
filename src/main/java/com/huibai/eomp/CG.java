package com.huibai.eomp;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

public class CG {
    public static void main(String[] args) {
        // 1. 数据库配置
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/eomp?serverTimezone=Asia/Shanghai", "root", "159357")
                .globalConfig(builder -> {
                    builder.author("zhou") // 设置作者
                            .outputDir(System.getProperty("user.dir") + "/src/main/java") // 输出目录
                            .disableOpenDir(); // 禁止打开输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.huibai.eomp") // 设置父包名
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // XML 输出路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(
                                    // 系统管理核心
                                    "users", "roles", "permissions",
                                    "user_roles", "role_permissions", "jwt_blacklist",
                                    // 组织架构与公告
                                    "departments", "employees", "announcements", "announcement_views",
                                    // 考勤与请假
                                    "attendance_records", "attendance_rules",
                                    "leave_applications", "leave_types",
                                    // 项目管理
                                    "projects", "project_members"
                            )
                            // Entity 策略配置
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()

                            // Controller 策略配置
                            .controllerBuilder()
                            .enableRestStyle();
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}