# 系统架构开发文档

（以下为根据前述六大架构内容整理的系统级开发文档，结构清晰，可继续扩展。）

---

## 1. 概述

本开发文档描述了企业内部管理平台的整体架构设计，包括六个核心架构视图：整体架构、前端架构、后端架构、数据库架构、部署架构、安全架构。文档用于指导系统的设计、开发、部署与后期维护。

---

## 2. 整体架构（Overall Architecture）

系统采用前后端分离架构，由 **Vue 3 前端 + Spring Boot 3 后端 + MySQL 数据库** 构成。用户通过浏览器访问前端页面，前端调用后端 REST API，并通过 JWT 进行授权与身份认证。

系统由以下主要子系统构成：

* 前端 Web SPA（Vue）
* 后端 API 服务（Spring Boot）
* 数据库服务（MySQL）
* 静态资源服务器（Nginx）
* 权限与安全框架（Spring Security + JWT）
* 日志与审计

关系说明：

* 前端 → 后端：HTTP/HTTPS + Token
* 后端 ↔ 数据库：JDBC/MyBatis-Plus
* 管理员可通过后台管理界面配置权限、角色、部门等基础数据

---

## 3. 前端架构（Frontend Architecture）

前端使用 Vue 3 + Vite 进行构建，采用组件化、模块化模式开发。

### 前端结构层次

* **页面层（Views）**：登录、首页、用户管理、部门管理、考勤、请假、项目、公告等
* **组件层（Components）**：表格组件、表单组件、弹窗组件等通用 UI
* **路由层（Vue Router）**：根据用户角色加载不同路由表
* **状态管理层（Pinia）**：用户信息、权限、系统配置、缓存
* **网络层（Axios）**：封装请求与响应拦截，自动注入 JWT
* **UI 组件库**：Element Plus 或 Ant Design Vue

### 前端功能模块

* RBAC 动态菜单渲染
* 前端路由鉴权
* 表格与分页查询统一封装
* 表单校验
* 图表展示（考勤统计、项目统计）

---

## 4. 后端架构（Backend Architecture）

后端采用 Spring Boot 3.4，整体架构遵循经典的三层结构。

### 后端分层架构

* **Controller 层**：暴露 REST API，进行数据接收与响应包装
* **Service 层**：业务逻辑处理，事务控制
* **Mapper 层（MyBatis-Plus）**：数据库访问
* **Entity 层**：实体类与数据库表结构对应
* **Security 模块**：认证、鉴权、Token 过滤器
* **Utils/Config 模块**：系统工具与统一配置

### 核心功能模块

* 用户管理
* 角色权限 RBAC
* 部门组织架构
* 员工信息
* 考勤（规则、打卡、统计）
* 请假（申请、审批）
* 项目管理（信息、成员、进度）
* 公告通知
* 系统日志审计

### 技术框架

* Spring Boot
* Spring Security
* JWT
* MyBatis-Plus
* Maven

---

## 5. 数据库架构（Database Architecture）

数据库采用 MySQL 8，按照第三范式（3NF）设计。

### 核心实体关系

* **用户表**：user
* **角色表**：role
* **用户角色关系表**：user_role
* **权限表**：permission
* **角色权限关系表**：role_permission
* **部门表**：department
* **员工表**：employee
* **考勤记录表**：attendance_record
* **考勤规则表**：attendance_rule
* **请假申请表**：leave_request
* **项目表**：project
* **项目成员表**：project_member
* **公告表**：notice

### 关键关系说明

* 一个用户可对应一个或多个角色
* 一个角色可对应多个权限点
* 一个部门可包含多个员工
* 一个员工可参与多个项目

---

## 6. 部署架构（Deployment Architecture）

系统支持本地部署与云服务器部署。

### 部署结构

* **Nginx**：用于前端静态资源托管 + 反向代理后端接口
* **Spring Boot 服务**：部署在 Linux 服务器上（如 CentOS/Ubuntu）
* **MySQL 数据库**：可与后端同机或独立服务器部署
* **HTTPS 配置**：通过 Nginx 实现 SSL 证书绑定

### 部署方式

* 前端：Vite 打包生成 dist → Nginx
* 后端：Jar 包或 Docker 部署
* 数据库：MySQL 物理服务器或 Docker

---

## 7. 安全架构（Security Architecture）

系统采用多层级安全策略：

### 身份认证

* 用户登录成功后颁发 JWT
* Token 由前端存储于 localStorage 或 Cookie
* 后端通过过滤器解析 Token

### 权限控制（RBAC）

* 用户 → 角色 → 权限点
* 接口访问前检查权限并决定是否允许访问
* 前端根据权限动态显示菜单与按钮

### 数据安全

* 密码使用 BCrypt 加密
* SQL 预编译防 SQL 注入
* 全局 XSS 过滤
* 操作日志记录关键行为

---

## 8. 总结

本开发文档从六个方面完整描述了系统的架构设计，为后续详细设计、编码实现、部署上线与系统维护提供蓝图支持。开发人员可依据本架构文档规范开展开发工作，同时留有良好的扩展性与升级空间。

---

