以下是整合后的完整 `README.md` 文件：

---

# PatPat Pro

**PatPat Pro** 是一个用于管理和处理学生代码、考试成绩及代码相似性分析的Java项目。项目集成了腾讯云对象存储（COS）用于文件上传下载，使用MySQL数据库进行数据管理，并通过Swing实现用户界面，提供学生作业提交、助教评审、代码查重、成绩导出等核心功能。

---

## 功能概述

### 核心功能
1. **代码管理**  
   - 学生在客户端提交代码上传至腾讯云COS，支持`.zip`格式上传。
   - 助教下载代码进行批改或分析，支持批量下载和历史版本管理。
   
2. **成绩管理**  
   - 成绩存储于MySQL数据库，支持导出为Excel文件。
   - 自动计算排名，按课程、学生、时间维度统计分析。

3. **代码相似性分析**  
   - 基于**AST（抽象语法树）**和**MurmurHash算法**实现代码查重。
   - 支持多项目对比，生成相似性报告，阈值可配置。

4. **用户管理**  
   - 学生/助教双角色登录，密码复杂度校验。
   - 个人信息维护（如密码修改、学号绑定）。

5. **通知系统**  
   - 实时发布课程公告，学生端和助教端同步显示。

---

## 项目结构

```
src/
├── team.patpat.cloudobjectstorage/     # 腾讯云对象存储操作
│   ├── DownloadMyCode.java             # 下载学生代码
│   ├── DownloadStandardAnswer.java     # 下载标准答案
│   ├── UploadStudentCode.java          # 上传学生代码
│   └── ...                             # 其他COS操作类
├── team.patpat.codereview/             # 代码审查与查重
│   ├── ASTHash.java                    # AST解析与哈希生成
│   ├── MurmurHash.java                 # MurmurHash算法实现
│   └── UnionReview.java                # 综合相似性分析
├── team.patpat.database/               # 数据库操作
│   ├── AssistantLogin.java             # 助教登录验证
│   ├── ExportGrades.java               # 导出成绩到Excel
│   ├── GetHashValue.java               # 获取哈希值
│   └── ...                             # 其他数据库工具类
├── team.patpat.filerunning/            # 文件处理与程序运行
│   ├── FileProcess.java                # 文件读写、比较
│   ├── JudgeUser.java                  # 代码运行与评审
│   └── ProgramHandle.java              # 程序编译与输出检查
└── team.patpat.view/                   # 用户界面（Swing实现）
    ├── Login.java                      # 登录界面
    ├── Homework.java                   # 作业提交界面
    ├── Ranks.java                      # 成绩排名界面
    └── ...                             # 其他UI组件
```

---

## 依赖项

| 依赖                | 用途                          |
|---------------------|-----------------------------|
| **腾讯云COS SDK**    | 实现文件上传、下载到腾讯云对象存储       |
| **MySQL Connector/J** | 连接MySQL数据库，管理成绩和用户数据    |
| **Apache POI**       | 生成Excel文件（成绩导出）           |
| **JavaParser**       | 解析Java代码生成AST用于相似性分析     |
| **java.util.logging** | 记录系统日志（错误、操作记录）         |

---

## 快速开始

### 环境配置
1. **数据库配置**  
   修改 `team.patpat.database.GV.java` 中的数据库连接信息：
   ```java
   public static final String url = "jdbc:mysql://localhost:3306/patpat";
   public static final String user = "root";
   public static final String password = "123456";
   ```

2. **腾讯云COS配置**  
   在 `GV.java` 中填写腾讯云凭据：
   ```java
   public static final String secretId = "YOUR_SECRET_ID";
   public static final String secretKey = "YOUR_SECRET_KEY";
   public static final String region = "ap-beijing";
   public static final String bucketName = "patpat-bucket";
   ```

### 运行步骤
1. 克隆仓库并导入IDE（推荐IntelliJ IDEA）：
   ```bash
   git clone https://gitee.com/sethan/pat-pat.git
   ```
2. 配置依赖项（Maven或手动导入JAR包）。
3. 运行 `team.patpat.view.Login` 启动登录界面。

---

## 使用示例

### 学生提交作业
1. 登录后进入 **作业提交** 界面。
2. 选择课程（如“迭代一”），上传代码压缩包（`.zip`）。
3. 系统自动解压并运行代码，返回测试结果（AC/WA/TLE等）。

### 助教管理成绩
1. 登录后进入 **成绩管理** 界面。
2. 下载学生代码，运行批量查重：
   ```java
   // 示例：代码相似性分析
   ProjectSimilarity comparator = new ProjectSimilarity();
   double similarity = comparator.compareProjects("project1.zip", "project2.zip", 800);
   System.out.println("相似度: " + similarity + "%");
   ```
3. 导出成绩到Excel：
   ```java
   ExportGrades exporter = new ExportGrades();
   exporter.exportGrades("/path/to/grades.xlsx");
   ```

---

## 日志与调试
- **日志路径**：`./log/`
  - `globalError.log`：全局错误日志（如文件读写失败）。
  - `programHandleError.log`：程序运行错误日志。
- **调试建议**：通过日志定位错误，确保COS和数据库连接配置正确。

---

## 注意事项
1. 代码文件必须为`.zip`格式，且根目录包含`src/`和`out/`文件夹。
2. 相似性阈值默认设置为70%，可在 `UnionReview.java` 中调整。
3. 定期清理日志文件，避免磁盘空间占用过多。

---

## 许可证
本项目采用 [MIT License](LICENSE)，欢迎贡献代码或提交Issue。