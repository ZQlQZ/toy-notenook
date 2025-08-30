# 项目接口文档

## 接口分类

API接口按功能分为以下几类：

1. **用户管理**：用户登录等相关接口
2. **笔记管理**：笔记的增删改查接口
3. **AI服务**：AI分析、总结和问答接口

## 接口详情

### 用户管理接口

- POST /api/users/login - 用户登录

### 笔记管理接口

- GET /api/notes/user/{userId} - 获取用户笔记列表
- GET /api/notes/{id} - 获取笔记详情
- POST /api/notes - 创建笔记
- PUT /api/notes - 更新笔记
- DELETE /api/notes/{id} - 删除笔记

### AI服务接口

- POST /api/ai/summarize - 总结笔记