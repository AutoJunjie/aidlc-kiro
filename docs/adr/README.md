# Architecture Decision Records (ADR)

## 什么是 ADR？

Architecture Decision Record (ADR) 是一种记录重要架构决策的文档格式。每个 ADR 描述一个具体的架构决策，包括背景、决策内容、考虑的替代方案以及决策的后果。

## ADR 格式

每个 ADR 文档应包含以下部分：

1. **标题**: 简短描述决策内容
2. **状态**: Proposed（提议）/ Accepted（接受）/ Deprecated（废弃）/ Superseded（被取代）
3. **日期**: 决策日期
4. **背景**: 为什么需要做这个决策？当前面临什么问题？
5. **决策**: 我们决定做什么？
6. **替代方案**: 考虑过哪些其他方案？为什么没有选择它们？
7. **后果**: 这个决策会带来什么影响？有哪些优点和缺点？

## ADR 列表

| ADR | 标题 | 状态 | 日期 |
|-----|------|------|------|
| [ADR-001](./001-adopt-ddd-architecture.md) | 采用领域驱动设计（DDD）架构 | Accepted | 2025-11-10 |
| [ADR-002](./002-use-postgresql-as-database.md) | 使用 PostgreSQL 作为主数据库 | Accepted | 2025-11-10 |
| [ADR-003](./003-frontend-backend-separation.md) | 采用前后端分离架构 | Accepted | 2025-11-10 |
| [ADR-004](./004-use-aggregate-root-pattern.md) | 使用聚合根模式管理事务边界 | Accepted | 2025-11-10 |
| [ADR-005](./005-use-react-query-for-state-management.md) | 使用 React Query 进行状态管理 | Accepted | 2025-11-10 |

## 如何创建新的 ADR

1. 复制 `template.md` 文件
2. 重命名为 `XXX-short-title.md`（XXX 是递增的编号）
3. 填写所有必需的部分
4. 在本 README 的 ADR 列表中添加新条目
5. 提交到版本控制系统

## 参考资料

- [ADR GitHub Organization](https://adr.github.io/)
- [Documenting Architecture Decisions](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions)
