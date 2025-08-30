# Toy-Notebook

2025南开大学软件学院暑期实训week2作业

## 功能特性

1. 用户模块：
   - 用户登录功能
   - 用户注册功能

2. 笔记模块：
   - 笔记的增删改查功能
   - 查看笔记列表
   - 创建新笔记
   - 编辑现有笔记
   - 删除笔记

3. AI大模型API调用：
   - 笔记内容分析
   - 笔记内容总结
   - 基于笔记内容的智能问答

## 技术栈

### 后端
- Java 24
- Spring Boot 3.5.0
- MyBatis-Plus 3.5.6
- MySQL 9.4.0
- Maven 3.9.11

### 前端
- Vue.js 2.6.14
- Axios 1.7.4
- Vue Router 3.6.5

## 项目结构

```
toy-notenook/
├── database.sql                    # 数据库初始化脚本
├── API_DOCUMENTATION.md           # API接口文档
├── noteapp-backend/               # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/project/
│   │   │   │   ├── ProjectApplication.java    # 应用启动类
│   │   │   │   ├── common/                    # 公共类
│   │   │   │   ├── config/                    # 配置类
│   │   │   │   ├── controller/                # 控制器
│   │   │   │   ├── dto/                       # 数据传输对象
│   │   │   │   ├── entity/                    # 实体类
│   │   │   │   ├── mapper/                    # Mapper接口
│   │   │   │   └── service/                   # 服务层
│   │   │   └── resources/
│   │   │       └── application.yml            # 配置文件
│   └── pom.xml                               # Maven配置
└── noteapp-frontend/              # 前端项目
    ├── src/
    │   ├── App.vue                           # 根组件
    │   ├── main.js                           # 入口文件
    │   ├── views/                            # 页面组件
    │   │   ├── Login.vue                     # 登录页面
    │   │   └── NoteList.vue                  # 笔记列表页面
    │   ├── services/                         # 服务层
    │   │   ├── aiService.js                  # AI服务
    │   │   ├── authService.js                # 认证服务
    │   │   ├── config.js                     # 配置文件
    │   │   ├── httpClient.js                 # HTTP客户端
    │   │   ├── index.js                      # 服务索引
    │   │   ├── noteService.js                # 笔记服务
    │   │   └── utils.js                      # 工具函数
    │   ├── router/                           # 路由配置
    │   │   └── index.js                      # 路由入口
    │   └── assets/                           # 静态资源
    ├── vue.config.js                         # Vue配置
    └── package.json                          # npm配置
```

## 运行项目

### 数据库准备

1. 表忘记先安装mysql

2. 创建MySQL数据库：
   安装sql后运行：

   ```sql
   CREATE DATABASE demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. 执行数据库初始化脚本：
   ```bash
   mysql -u root -p demo < database.sql
   ```

4. 设置用户和密码
   ```sql
   USE demo;
   INSERT INTO user (username, password) VALUES ('zql', '123456');
   ```

### 后端运行

1. 进入后端项目目录：
   ```bash
   cd noteapp-backend
   ```

2. 修改数据库配置（如有需要）：
   在 `src/main/resources/application.yml` 文件中修改数据库密码和火山API。

3. 编译并运行后端项目：
   ```bash
   mvn spring-boot:run
   ```

   或者打包运行：
   ```bash
   mvn clean package
   java -jar target/springboot-notebook-demo-1.0.1.jar
   ```

### 前端运行

1. 进入前端项目目录：
   ```bash
   cd noteapp-frontend
   ```

2. 安装依赖：
   ```bash
   npm install
   ```

3. 运行前端项目：
   ```bash
   npm run serve
   ```

4. 访问应用：
   打开浏览器访问 `http://localhost:8081`

## API文档

详细的API接口文档请参考 [API_DOCUMENTATION.md](API_DOCUMENTATION.md) 文件。

## 默认用户

项目中预设了以下测试用户：

- 用户名：zql，密码：123456

## 进度
- 连接到了数据库
- 实现了用户登录功能
- 实现了笔记的增删改查功能
- 实现了AI总结功能

## 注意事项

1. 请确保MySQL服务正在运行
2. 请根据实际环境修改数据库连接配置
3. 前端默认访问端口为8081，后端默认访问端口为8080
4. AI功能需要配置火山AI API密钥才能正常使用




