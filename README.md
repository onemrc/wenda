# Wenda - 问答社区系统

## 项目简介

Wenda 是一个基于 Spring Boot 的问答社区系统，类似于知乎的问答平台。用户可以发布问题、回答问题、关注用户和问题、点赞评论等。这是一个毕业设计项目，实现了完整的问答社区功能。

## 核心功能

### 用户系统
- **用户注册/登录**：支持手机号和邮箱注册登录
- **用户认证**：支持学生和教师身份认证
- **个人信息管理**：修改用户名、密码、个人介绍等
- **用户资料**：头像、性别、院系、专业、入学年份等

### 问答系统
- **问题发布**：用户可以发布问题，支持匿名发布
- **问题浏览**：最新问题列表、热门问题排行
- **问题搜索**：基于 Elasticsearch 的全文搜索
- **问题详情**：查看问题详情、回答列表、关注者信息

### 回答系统
- **回答发布**：对问题进行回答
- **回答管理**：删除自己的回答
- **回答互动**：点赞/取消点赞回答

### 社交功能
- **用户关注**：关注/取消关注其他用户
- **问题关注**：关注/取消关注感兴趣的问题
- **私信系统**：用户间私信交流
- **推荐系统**：推荐用户关注

### 其他功能
- **收藏功能**：收藏问题和回答
- **标签系统**：问题分类标签
- **敏感词过滤**：内容安全过滤
- **异步消息**：基于 Redis 的异步事件处理

## 技术架构

### 后端技术栈
- **框架**：Spring Boot 2.0.6
- **数据库**：MySQL 5.7
- **ORM**：MyBatis
- **缓存**：Redis
- **搜索引擎**：Elasticsearch
- **连接池**：Druid
- **模板引擎**：FreeMarker
- **异步处理**：Spring AOP + Redis 队列
- **工具库**：Lombok、FastJSON、Jsoup、Tess4j

### 前端技术
- **模板引擎**：FreeMarker
- **样式框架**：Bootstrap
- **JavaScript**：jQuery + 自定义组件
- **富文本编辑**：RichtextEditor

### 数据库设计
- **用户表**：存储用户基本信息
- **问题表**：存储问题内容
- **评论表**：存储回答和评论
- **消息表**：存储私信
- **收藏表**：存储收藏关系
- **标签表**：存储问题标签
- **认证表**：存储用户身份认证信息

### 缓存设计
- **用户会话**：Redis 存储用户登录状态
- **关注关系**：Redis Set 存储用户关注关系
- **点赞数据**：Redis 存储点赞信息
- **热门排行**：Redis ZSet 实现排行榜
- **异步队列**：Redis List 实现事件队列

## 项目结构

```
src/main/java/com/demo/wenda/
├── aspect/          # AOP 切面（日志、热门排行等）
├── async/           # 异步事件处理
├── controller/      # 控制器层
├── dao/            # 数据访问层
├── domain/         # 实体类
├── dto/            # 数据传输对象
├── elasticSearch/  # Elasticsearch 集成
├── enums/          # 枚举类
├── interceptor/    # 拦截器
├── redis/          # Redis 配置和工具
├── service/        # 业务逻辑层
├── utils/          # 工具类
└── vo/             # 视图对象
```

## 核心特性

### 1. 异步事件处理
- 基于 Redis 队列的异步事件系统
- 支持点赞、关注、评论等事件的异步处理
- 提高系统响应性能和用户体验

### 2. 全文搜索
- 集成 Elasticsearch 实现问题全文搜索
- 支持标题和内容的多字段搜索
- 提供实时搜索体验

### 3. 缓存优化
- Redis 缓存用户会话、关注关系、点赞数据
- 热门问题排行榜实时更新
- 减少数据库压力，提升系统性能

### 4. 安全机制
- 密码 MD5 + Salt 加密
- 敏感词过滤
- HTML 标签转义
- 用户身份认证

### 5. 响应式设计
- 基于 Bootstrap 的响应式界面
- 支持移动端访问
- 现代化的用户界面

## 部署说明

### 环境要求
- JDK 1.8+
- MySQL 5.7+
- Redis 3.0+
- Elasticsearch 5.x
- Maven 3.0+

### 配置说明
1. 修改 `application.yml` 中的数据库连接信息
2. 配置 Redis 连接信息
3. 配置 Elasticsearch 集群节点
4. 执行 `wenda.sql` 初始化数据库

### 启动步骤
1. 克隆项目：`git clone [repository-url]`
2. 导入数据库：执行 `src/main/resources/wenda.sql`
3. 修改配置：更新 `application.yml` 中的连接信息
4. 启动服务：`mvn spring-boot:run`
5. 访问系统：`http://localhost:8080`

## API 接口

### 用户相关
- `POST /reg` - 用户注册
- `POST /do_login` - 用户登录
- `GET /logout` - 用户登出
- `GET /user/{userId}` - 用户主页
- `POST /editUserName/{userId}` - 修改用户名
- `POST /editUserPass/{userId}` - 修改密码

### 问题相关
- `POST /question/add` - 发布问题
- `GET /question/{questionId}` - 问题详情
- `GET /searchQuestion` - 搜索问题
- `GET /new` - 最新问题列表
- `GET /hot` - 热门问题列表

### 回答相关
- `POST /addAnswer` - 添加回答
- `POST /deleteAnswer` - 删除回答

### 社交功能
- `POST /followUser` - 关注用户
- `POST /unfollowUser` - 取消关注用户
- `POST /followQuestion` - 关注问题
- `POST /unfollowQuestion` - 取消关注问题
- `POST /like` - 点赞回答
- `POST /dislike` - 取消点赞



## 许可

本项目仅用于学习和研究目的。
