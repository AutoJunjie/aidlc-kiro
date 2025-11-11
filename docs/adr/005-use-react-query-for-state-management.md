# ADR-005: 使用 React Query 进行状态管理

## 状态
Accepted

## 日期
2025-11-10

## 背景

React 前端应用需要管理两类状态：
1. **服务器状态**：从后端 API 获取的数据（圈子、角色、提案、会议等）
2. **客户端状态**：UI 状态（模态框开关、表单输入等）

我们需要选择合适的状态管理方案来：
- 高效地获取和缓存服务器数据
- 处理加载和错误状态
- 实现乐观更新
- 减少样板代码
- 提供良好的开发体验

## 决策

我们决定使用 **React Query** 作为主要的状态管理库：

**用途**:
- 管理所有服务器状态（API 数据获取、缓存、同步）
- 自动处理加载、错误、重试逻辑
- 实现乐观更新和后台重新获取
- 管理分页和无限滚动

**客户端状态**:
- 使用 React 内置的 useState 和 useContext
- 简单的 UI 状态不需要额外的状态管理库

**不使用**:
- Redux 或其他全局状态管理库（除非有特殊需求）

## 替代方案

### 方案 1: Redux + Redux Toolkit
传统的全局状态管理方案。

**优点**:
- 成熟稳定，社区庞大
- 强大的开发工具（Redux DevTools）
- 可预测的状态管理
- 适合复杂的客户端状态

**缺点**:
- 样板代码多
- 学习曲线陡峭
- 需要手动处理异步逻辑（thunk 或 saga）
- 需要手动管理缓存和重新获取
- 对于服务器状态管理过于复杂

**为什么没有选择**: Redux 更适合管理复杂的客户端状态，但对于服务器状态管理来说过于复杂。我们的应用主要是数据驱动的，大部分状态来自服务器，Redux 会增加不必要的复杂度。

### 方案 2: SWR
Vercel 开发的数据获取库。

**优点**:
- 轻量级
- 简单易用
- 自动缓存和重新验证
- 支持 SSR

**缺点**:
- 功能相对简单
- 社区和生态系统不如 React Query
- 高级功能支持有限
- 文档不如 React Query 详细

**为什么没有选择**: SWR 和 React Query 功能类似，但 React Query 功能更强大，社区更活跃，文档更完善。React Query 提供了更多的高级特性（如并行查询、依赖查询、无限查询等）。

### 方案 3: Apollo Client
GraphQL 客户端，也可以用于 REST API。

**优点**:
- 强大的缓存机制
- 完整的 GraphQL 支持
- 丰富的功能

**缺点**:
- 主要为 GraphQL 设计
- 对 REST API 支持不够友好
- 学习曲线陡峭
- 包体积大
- 对于 REST API 来说过度设计

**为什么没有选择**: 我们使用 REST API 而非 GraphQL，Apollo Client 不是最佳选择。

### 方案 4: 自己实现数据获取逻辑
使用 useEffect + useState 手动管理数据获取。

**优点**:
- 完全控制
- 无额外依赖
- 简单直接

**缺点**:
- 需要大量样板代码
- 需要手动处理缓存、重试、错误等
- 容易出错
- 难以维护
- 重复造轮子

**为什么没有选择**: 手动实现会导致大量重复代码和潜在的 bug。使用成熟的库可以节省开发时间并提供更好的用户体验。

## 后果

### 正面影响
- **减少样板代码**：无需手动编写 loading、error、data 状态管理
- **自动缓存**：数据自动缓存，减少不必要的网络请求
- **后台重新获取**：数据在后台自动更新，保持新鲜
- **乐观更新**：支持乐观更新，提升用户体验
- **开发体验好**：提供 DevTools，易于调试
- **性能优化**：自动去重请求，减少网络开销
- **易于测试**：提供测试工具，易于编写测试
- **类型安全**：与 TypeScript 完美集成

### 负面影响
- **学习曲线**：团队需要学习 React Query 的概念和 API
- **额外依赖**：增加了一个外部依赖
- **缓存策略**：需要理解和配置缓存策略

### 风险
- **过度缓存**：不当的缓存配置可能导致数据不新鲜
- **内存占用**：缓存过多数据可能增加内存占用
- **依赖锁定**：依赖特定库，未来迁移成本

### 缓解措施
- 提供 React Query 培训和最佳实践文档
- 配置合理的缓存策略（staleTime, cacheTime）
- 使用 React Query DevTools 监控缓存状态
- 为关键数据设置较短的 staleTime
- 实施代码审查，确保正确使用
- 编写自定义 hooks 封装常见的查询模式

## 使用示例

### 查询数据
```typescript
// hooks/useCircles.ts
export const useCircles = (filters?: CircleFilters) => {
  return useQuery({
    queryKey: ['circles', filters],
    queryFn: () => circleApi.getCircles(filters),
    staleTime: 30000, // 30 seconds
  });
};

// 在组件中使用
const { data: circles, isLoading, error } = useCircles();
```

### 修改数据
```typescript
// hooks/useCreateCircle.ts
export const useCreateCircle = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (data: CreateCircleRequest) => circleApi.createCircle(data),
    onSuccess: () => {
      // 使缓存失效，触发重新获取
      queryClient.invalidateQueries({ queryKey: ['circles'] });
    },
  });
};

// 在组件中使用
const createCircle = useCreateCircle();
const handleSubmit = (data) => {
  createCircle.mutate(data);
};
```

### 乐观更新
```typescript
export const useUpdateCircle = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ id, data }: UpdateCircleParams) => 
      circleApi.updateCircle(id, data),
    onMutate: async ({ id, data }) => {
      // 取消正在进行的查询
      await queryClient.cancelQueries({ queryKey: ['circle', id] });
      
      // 保存之前的数据
      const previousCircle = queryClient.getQueryData(['circle', id]);
      
      // 乐观更新
      queryClient.setQueryData(['circle', id], (old) => ({
        ...old,
        ...data,
      }));
      
      return { previousCircle };
    },
    onError: (err, variables, context) => {
      // 回滚
      queryClient.setQueryData(
        ['circle', variables.id],
        context.previousCircle
      );
    },
    onSettled: (data, error, variables) => {
      // 重新获取确保数据一致
      queryClient.invalidateQueries({ queryKey: ['circle', variables.id] });
    },
  });
};
```

## 相关决策
- [ADR-003](./003-frontend-backend-separation.md): 采用前后端分离架构

## 参考资料
- [React Query Official Documentation](https://tanstack.com/query/latest)
- [React Query Best Practices](https://tkdodo.eu/blog/practical-react-query)
- [Comparison: React Query vs SWR vs Apollo](https://react-query.tanstack.com/comparison)
