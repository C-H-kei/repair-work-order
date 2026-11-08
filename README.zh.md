# 园区设备报修运维系统 (Repair Work Order System)

##  项目简介
基于 Spring Boot 3 + Vue 3 开发的前后端分离 B 端运维平台。解决园区、物业公司设备报修流程无追踪、状态不透明、维修人员响应慢的痛点，实现从“选择设备 -> 提交报修 -> 接单维修 -> 维修完成”的全生命周期闭环管理。

##  技术栈
- **后端**：Java 17、Spring Boot 3.3、MyBatis-Plus、MySQL 8.0
- **前端**：Vue 3、TypeScript、Vite、Element Plus、Axios
- **缓存与安全**：Redis、JWT (JSON Web Token)、Spring MVC 拦截器
- **接口文档**：Knife4j (OpenAPI 3)

##  核心业务功能
1. **安全认证**：基于 JWT 的无状态登录鉴权，拦截未授权接口访问。
2. **设备台账管理**：设备的录入、分页查询与状态展示。
3. **报修工单流转**：用户提交报修，维修人员接单，直至完单。
4. **状态机防越权**：内置严密的工单状态机（待处理 -> 维修中 -> 已完成），在后端进行强校验，防止非法篡改。
5. **多表联查优化**：采用 VO (View Object) 模式与左连接查询，解决 N+1 问题，工单列表直接展示设备名称。

## 核心技术亮点解析
- **全局异常处理**：基于 `@RestControllerAdvice` 实现全局异常捕获，统一封装 `Result` 响应格式，防止底层报错（如 SQL 错误、状态越权）直接暴露给前端。
- **Redis 缓存优化**：基于 Cache-Aside（旁路缓存）模式，对设备列表查询接口加入 Redis 缓存，降低 MySQL 读压力；并在新增/修改数据时主动删除缓存，保证数据最终一致性。
- **前后端联调规范**：提供集成 Knife4j 的在线接口文档，同时前端基于 Axios 实现请求拦截器统一携带 Token，响应拦截器统一处理业务响应码。

##  项目结构
```text
repair-work-order/
├── backend/          # 后端 Spring Boot 源码
│   ├── src/main/java/com/dormitory/repairbackend/
│   │   ├── controller/   # 接口层
│   │   ├── service/      # 业务逻辑层
│   │   ├── mapper/       # 数据访问层
│   │   ├── entity/       # 实体类及 VO
│   │   ├── config/       # 配置类 (Redis, Knife4j 等)
│   │   └── exception/    # 全局异常拦截与自定义异常
└── frontend/         # 前端 Vue3 + TS 源码
    ├── src/utils/    # Axios 封装
    └── src/App.vue   # 核心业务页面