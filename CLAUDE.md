# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build / Run

```bash
# 编译 + 运行
mvn spring-boot:run

# 仅编译
mvn compile

# 运行测试
mvn test

# 运行代码生成器（生成 entity/mapper/service/controller）
mvn test-compile exec:java -Dexec.mainClass="com.langdada.backend.CodeGenerator" -Dexec.classpathScope=test

# 打包
mvn package -DskipTests
```

应用启动后，API 文档地址：`http://localhost:8080/api/doc.html`（Knife4j）。

## 技术栈

- **Spring Boot 2.7.18** / Java 11 / Maven
- **MyBatis-Plus 3.5.16**（BOM 统一管理版本）
- **MySQL 8.0.33**
- **Knife4j** 生成接口文档（OpenAPI 2 模式）
- **Hutool 5.8.46** 工具库
- **Velocity** 模板引擎（仅代码生成器用）

## 架构分层

```
controller  →  service (I*Service)  →  service.impl (*ServiceImpl)  →  mapper (*Mapper)  →  entity
     ↑                                        ↑
  @AuthCheck AOP 鉴权               extends ServiceImpl<Mapper, Entity>
```

### Controller 层
- 统一返回 `BaseResponse<T>`，通过 `ResultUtils.success(data)` / `ResultUtils.error(...)` 构建。
- 分页请求使用 `PageRequest`（current / pageSize / sortField / sortOrder），按 ID 删除使用 `DeleteRequest`（仅携带 id）。
- 当前仅 `UserController` 实现了完整的注册/登录/登出/获取当前用户；`AppController`、`QuestionController` 等其余 controller 为代码生成器生成的空壳，待补充业务逻辑。

### Service 层
- 接口继承 MyBatis-Plus 的 `IService<Entity>`，实现类继承 `ServiceImpl<Mapper, Entity>`。
- `IUserService` 额外定义了 `userRegister`、`userLogin`、`userLogout`、`getLoginUser` 等认证相关方法。

### 认证与鉴权
- **登录态**：基于 `HttpSession`，登录成功后将 `User` 对象存入 `session.setAttribute("user_login", user)`。
- **权限注解**：`@AuthCheck(mustRole = "admin")` 标注在 Controller 方法上，由 `AuthInterceptor`（AOP `@Around`）校验当前用户角色。
- **获取当前用户**：`userService.getLoginUser(request)` 从 session 取 `user_login` 属性并回查数据库。
- **角色枚举**：`UserRoleEnum`（USER / ADMIN / BAN），常量定义在 `UserConstant`。
- **密码加密**：MD5 + 固定盐值 `"lang"`（`DigestUtils.md5DigestAsHex`）。

### 异常处理
- `BusinessException(code, message)` 携带业务错误码。
- `ErrorCode` 枚举定义标准错误码：`PARAMS_ERROR(40000)`、`NOT_LOGIN_ERROR(40100)`、`NO_AUTH_ERROR(40101)`、`SYSTEM_ERROR(50000)` 等。
- `ThrowUtils.throwIf(condition, errorCode, message)` 用于 guard clause 风格的条件校验。
- `GlobalExceptionHandler`（`@RestControllerAdvice`）统一拦截 `BusinessException` 和 `RuntimeException`，转换为 `BaseResponse`。

### Entity 设计约定
- 不使用 `@Data`，而是 `@Getter` + `@Setter` + `@ToString`。
- 每个字段显式标注 `@TableField("fieldName")`，因为 `application.yml` 中 `map-underscore-to-camel-case: false`。
- 主键使用 `@TableId(value = "id", type = IdType.AUTO)`。
- 所有实体包含 `createTime`、`updateTime`、`isDelete` 三个公共字段（isDelete 为逻辑删除标记，在 MyBatis-Plus 全局 `db-config` 中配置）。
- 实体位于 `model.entity` 包，视图对象（VO）位于 `model.vo`，请求 DTO 位于 `model.dto`，常量位于 `model.constant`，枚举位于 `model.enums`。

### 数据源
- 数据库名 `langdada`，连接 `localhost:3306`，用户名 `root`，密码 `1234`。
- 初始化脚本：`schema.sql`（建表）和 `data.sql`（种子数据），均为 classpath 下直接放置的手动执行脚本（非自动初始化）。
- 表：`user`、`app`、`question`、`scoring_result`、`user_answer`，构成一个测评/答题类应用的数据模型。

## 代码生成器

位于 `src/test/java/com/langdada/backend/CodeGenerator.java`，使用 MyBatis-Plus 3.5.16 新 API（`FastAutoGenerator` + Velocity），运行 main 方法即可根据数据库表重新生成 entity / mapper / service / controller / mapper.xml。配置要点：
- `remarks=true&useInformationSchema=true` 确保读取 MySQL 表注释。
- `tinyint(1)` → Boolean，`tinyint(n>1)` → Integer。
- Entity 放在 `model.entity`，XML 输出到 `src/main/resources/mapper/`。
