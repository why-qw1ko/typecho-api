# Typecho Blog API

基于 Spring Boot 3.2 + MyBatis Plus + JWT 的 Typecho 博客系统 RESTful API。

## 技术栈

- **Spring Boot 3.2.0**
- **MyBatis Plus 3.5.5**
- **Spring Security + JWT**
- **Knife4j (Swagger) 4.4.0**
- **MySQL 8.0**

## 功能模块

### 认证模块 `/api/auth`
- 用户登录（支持 Typecho MD5 密码和 BCrypt 密码）
- 用户注册
- 获取当前用户信息
- 修改密码

### 用户管理 `/api/users`
- 获取用户信息
- 用户列表（分页）
- 更新用户信息
- 删除用户

### 内容管理 `/api/contents`
- 文章/页面 CRUD
- 按作者、分类、标签筛选
- 浏览量统计

### 评论管理 `/api/comments`
- 评论 CRUD
- 评论审核（通过/拒绝/标记垃圾）

### 分类/标签管理 `/api/metas`
- 分类/标签 CRUD
- 按类型筛选

### 友情链接 `/api/links`
- 链接 CRUD

### 系统配置 `/api/options`
- 配置项管理

### 公开接口 `/api/public`
- 网站统计数据
- 健康检查

## 快速开始

### 1. 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 配置数据库
修改 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/typecho_slave?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 运行项目
```bash
mvn spring-boot:run
```

### 4. 访问接口文档
启动后访问：http://localhost:8080/doc.html

## API 认证说明

### 获取 Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "your_password"}'
```

### 使用 Token 访问受保护接口
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer your_token_here"
```

## 权限说明

| 角色 | 权限 |
|------|------|
| ADMIN | 所有权限 |
| EDITOR | 内容管理、评论管理 |
| CONTRIBUTOR | 创建和管理自己的内容 |
| SUBSCRIBER | 发表评论、修改个人信息 |
| VISITOR | 仅查看公开内容 |

## 项目结构

```
typecho-api/
├── src/main/java/com/typecho/
│   ├── common/          # 公共类（Result、异常处理等）
│   ├── config/          # 配置类（Security、Swagger等）
│   ├── controller/      # 控制器层
│   ├── dto/             # 数据传输对象
│   ├── entity/          # 实体类
│   ├── mapper/          # MyBatis Mapper
│   ├── security/        # 安全相关（JWT、过滤器等）
│   └── service/         # 服务层
└── src/main/resources/
    └── application.yml  # 配置文件
```

## 打包部署

```bash
mvn clean package -DskipTests
java -jar target/typecho-api-1.0.0.jar
```

## 注意事项

1. Typecho 原系统使用 MD5 加密密码，本系统兼容 MD5 和 BCrypt 两种加密方式
2. 新注册用户默认使用 BCrypt 加密，密码安全性更高
3. JWT Token 默认有效期为 24 小时
4. 生产环境请修改 JWT 密钥

## License

MIT
