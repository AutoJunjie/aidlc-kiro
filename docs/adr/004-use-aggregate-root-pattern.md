# ADR-004: 使用聚合根模式管理事务边界

## 状态
Accepted

## 日期
2025-11-10

## 背景

在 DDD 架构中，我们需要定义清晰的事务边界来确保数据一致性。XHolacracy 系统有复杂的对象关系：
- 组织包含圈子，圈子包含角色和子圈子
- 提案包含紧张、问题、反应、反对、投票等
- 会议包含议程、参与者、会议记录等

我们需要决定：
1. 如何划分聚合边界？
2. 哪些对象应该作为聚合根？
3. 如何确保聚合内的一致性？
4. 如何处理聚合间的关系？

## 决策

我们决定使用 **聚合根（Aggregate Root）模式** 来管理事务边界，定义以下 4 个聚合根：

### 1. Organization Aggregate
- **聚合根**: Organization
- **包含**: Anchor Circle（通过组合关系）
- **边界**: 组织及其锚圈
- **职责**: 管理组织的基本信息和锚圈的创建

### 2. Circle Aggregate
- **聚合根**: Circle
- **包含**: Sub-Circles, Roles, SpecialRoles
- **边界**: 圈子及其直接包含的子圈子和角色
- **职责**: 管理圈子层次结构、角色创建和特殊角色分配

### 3. Proposal Aggregate
- **聚合根**: Proposal
- **包含**: Tension, Questions, Reactions, Amendments, Objections, Votes, DecisionEvents
- **边界**: 提案及其完整的决策流程
- **职责**: 管理提案的完整生命周期和集成决策流程

### 4. GovernanceMeeting Aggregate
- **聚合根**: GovernanceMeeting
- **包含**: MeetingAgenda, AgendaItems, MeetingRecord, Participants
- **边界**: 会议及其议程和记录
- **职责**: 管理会议的完整流程

### 聚合设计原则
1. **小聚合优先**: 聚合尽可能小，只包含必须保持一致性的对象
2. **通过 ID 引用**: 聚合间通过 ID 引用，而非对象引用
3. **事务边界**: 一个事务只修改一个聚合根
4. **最终一致性**: 聚合间的一致性通过领域事件实现最终一致性

## 替代方案

### 方案 1: 大聚合（Organization 作为唯一聚合根）
将 Organization 作为唯一聚合根，包含所有圈子、角色、提案、会议。

**优点**:
- 强一致性保证
- 简单直接
- 无需处理聚合间关系

**缺点**:
- 聚合过大，性能问题
- 并发冲突严重（所有操作都锁定整个组织）
- 难以扩展
- 违反 DDD 最佳实践

**为什么没有选择**: 聚合过大会导致严重的性能和并发问题。例如，两个用户同时在不同圈子创建角色会产生冲突。

### 方案 2: 细粒度聚合（每个实体都是聚合根）
将每个实体（Circle, Role, Proposal, Meeting 等）都作为独立的聚合根。

**优点**:
- 最大的并发性
- 灵活性高
- 易于扩展

**缺点**:
- 难以保证一致性
- 需要大量的最终一致性处理
- 复杂的事务协调
- 可能导致数据不一致

**为什么没有选择**: 过于细粒度会导致一致性问题。例如，创建圈子时必须同时创建四个特殊角色，如果它们是独立的聚合根，很难保证原子性。

### 方案 3: 无聚合根（传统 ORM 模式）
不使用聚合根概念，直接使用 JPA 实体和关系映射。

**优点**:
- 简单直接
- 开发速度快
- 无需考虑聚合边界

**缺点**:
- 缺乏明确的事务边界
- 业务规则分散
- 难以维护一致性
- 不符合 DDD 原则

**为什么没有选择**: 我们已经决定采用 DDD 架构（ADR-001），聚合根是 DDD 的核心概念。

## 后果

### 正面影响
- **清晰的事务边界**：每个聚合根定义了明确的事务边界
- **数据一致性保证**：聚合内的数据始终保持一致
- **并发性能优化**：不同聚合可以并发修改，减少锁冲突
- **业务规则封装**：聚合根封装了所有业务规则，确保对象始终有效
- **易于测试**：聚合根可以独立测试，无需复杂的依赖
- **扩展性**：聚合边界清晰，易于未来扩展

### 负面影响
- **学习曲线**：团队需要理解聚合根概念和设计原则
- **设计复杂度**：需要仔细设计聚合边界
- **跨聚合操作**：需要通过领域事件或应用服务协调
- **性能考虑**：需要平衡聚合大小和性能

### 风险
- **聚合边界划分错误**：边界划分不当可能导致性能问题或一致性问题
- **聚合过大**：聚合包含过多对象会影响性能
- **聚合过小**：聚合过小会导致一致性难以保证

### 缓解措施
- 遵循"小聚合优先"原则
- 定期审查聚合设计，根据实际使用情况调整
- 使用领域事件处理聚合间的最终一致性
- 编写详细的单元测试验证聚合不变量
- 监控聚合的性能指标，及时优化
- 文档化聚合边界和设计决策

## 聚合边界示例

### Circle Aggregate 边界
```
Circle (聚合根)
├── CircleId (值对象)
├── name, purpose, accountabilities
├── SpecialRoles (值对象)
│   ├── Circle Lead Assignment
│   ├── Facilitator Assignment
│   ├── Secretary Assignment
│   └── Circle Rep Assignment
├── Sub-Circles (通过 ID 引用)
└── Roles (实体，聚合内部)
    ├── RoleId
    ├── name, purpose, accountabilities
    ├── Domains (值对象)
    └── RoleAssignments (实体)
```

### Proposal Aggregate 边界
```
Proposal (聚合根)
├── ProposalId (值对象)
├── title, status
├── Tension (值对象)
├── CircleId (引用，不是聚合内部)
├── ProposerId (引用，不是聚合内部)
├── Questions (值对象集合)
├── Reactions (值对象集合)
├── Amendments (值对象集合)
├── Objections (实体集合)
├── Votes (值对象集合)
└── DecisionEvents (值对象集合)
```

## 相关决策
- [ADR-001](./001-adopt-ddd-architecture.md): 采用领域驱动设计（DDD）架构

## 参考资料
- Vaughn Vernon, "Effective Aggregate Design" (3-part series)
- Eric Evans, "Domain-Driven Design Reference"
- [Aggregate Design Best Practices](https://www.dddcommunity.org/library/vernon_2011/)
