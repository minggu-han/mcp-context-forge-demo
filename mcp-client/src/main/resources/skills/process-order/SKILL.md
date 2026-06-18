---
name: process-order
description: 处理客户订单的全流程
---

处理订单的步骤如下，请按顺序执行：

1. 调用 `validateOrder(orderId)` 工具验证订单 ID 是否有效。
2. 如果订单有效，调用 `reserveInventory(orderId)` 工具预留库存。
3. **仅当库存预留成功时**，调用 `chargePayment(orderId)` 工具进行扣款。
4. 扣款成功后，调用 `sendConfirmationEmail(orderId)` 工具发送确认邮件。
5. 如果上述任何步骤失败，立即调用 `rollbackOrder(orderId)` 回滚整个订单，并告知用户失败原因。