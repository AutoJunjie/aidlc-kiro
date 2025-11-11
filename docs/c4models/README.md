# C4 Models and Diagrams

## 概述

本目录包含 XHolacracy MVP 系统的架构模型和业务流程图，使用 PlantUML 格式编写。这些图表帮助理解系统的领域模型和业务流程。

## 文件列表

### 领域模型

- **[domain-model.puml](./domain-model.puml)** - 完整的 DDD 领域模型
  - 4 个聚合根：Organization, Circle, Proposal, GovernanceMeeting
  - 5 个实体：Role, RoleAssignment, Partner, Objection
  - 20+ 个值对象
  - 5 个枚举类型
  - 完整的关系映射

### 业务流程图

- **[business-process-overview.puml](./business-process-overview.puml)** - 业务流程概览
  - 展示整个系统的业务流程全景
  - 包含所有主要功能模块的交互

- **[business-process-circle-management.puml](./business-process-circle-management.puml)** - 圈子和角色管理流程
  - 圈子创建和层次结构管理
  - 角色创建、定义和分配
  - 特殊角色管理

- **[business-process-proposal-creation.puml](./business-process-proposal-creation.puml)** - 提案创建流程
  - 紧张识别和记录
  - 提案创建和提交
  - 添加到治理会议议程

- **[business-process-integrative-decision.puml](./business-process-integrative-decision.puml)** - 集成决策流程
  - 完整的六阶段流程
  - 提案阶段 → 澄清阶段 → 反应阶段 → 修改阶段 → 反对阶段 → 集成阶段
  - 反对验证和循环处理逻辑

- **[business-process-governance-meeting.puml](./business-process-governance-meeting.puml)** - 治理会议流程
  - 会议创建和安排
  - 签到轮、议程构建、处理议程项、结束轮
  - 会议记录和归档

- **[business-process-proposal-application.puml](./business-process-proposal-application.puml)** - 提案应用流程
  - 已批准提案的应用
  - 根据提案类型更新组织结构
  - 通知和可视化更新

## 如何查看这些图表

### 方法 1: 使用 PlantUML 在线编辑器
1. 访问 [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)
2. 复制 .puml 文件内容
3. 粘贴到编辑器中查看

### 方法 2: 使用 VS Code 插件
1. 安装 PlantUML 插件
2. 安装 Graphviz（PlantUML 依赖）
   ```bash
   # macOS
   brew install graphviz
   
   # Ubuntu/Debian
   sudo apt-get install graphviz
   
   # Windows
   choco install graphviz
   ```
3. 在 VS Code 中打开 .puml 文件
4. 使用 `Alt+D` (Windows/Linux) 或 `Option+D` (macOS) 预览

### 方法 3: 使用 PlantUML CLI
```bash
# 安装 PlantUML
brew install plantuml  # macOS
# 或下载 plantuml.jar

# 生成 PNG 图片
plantuml domain-model.puml

# 生成 SVG 图片
plantuml -tsvg domain-model.puml
```

### 方法 4: 使用 Docker
```bash
# 生成所有图表
docker run --rm -v $(pwd):/data plantuml/plantuml *.puml
```

## 图表说明

### 领域模型图

领域模型图展示了系统的核心业务对象及其关系：

- **聚合根**（Aggregate Root）：用红色标注，是事务边界
- **实体**（Entity）：有唯一标识的对象
- **值对象**（Value Object）：无唯一标识，通过值比较相等性
- **枚举**（Enum）：预定义的常量集合

关系类型：
- `*--`：组合关系（强拥有）
- `o--`：聚合关系（弱拥有）
- `-->`：关联关系
- `--|>`：继承关系

### 业务流程图

业务流程图使用泳道图（Swimlane Diagram）格式，清晰展示：
- 不同角色的职责（伙伴、圈引导、协调员、秘书等）
- 系统的自动化处理
- 流程的顺序和决策点
- 数据流和状态变化

## 维护指南

### 更新图表
1. 修改对应的 .puml 文件
2. 重新生成图片（如果需要）
3. 提交到版本控制

### 添加新图表
1. 创建新的 .puml 文件
2. 遵循现有的命名约定
3. 在本 README 中添加说明
4. 提交到版本控制

### 命名约定
- 领域模型：`domain-model-*.puml`
- 业务流程：`business-process-*.puml`
- 架构图：`architecture-*.puml`
- 部署图：`deployment-*.puml`

## 相关文档

- [Design Document](../../.kiro/specs/holacracy-management/design.md) - 详细的技术设计文档
- [Requirements Document](../../.kiro/specs/holacracy-management/requirements.md) - 需求文档
- [ADR](../adr/) - 架构决策记录

## 参考资料

- [PlantUML Official Documentation](https://plantuml.com/)
- [C4 Model](https://c4model.com/) - 软件架构可视化方法
- [Domain-Driven Design](https://www.domainlanguage.com/ddd/) - DDD 参考资料
