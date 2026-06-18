package com.demo.cmp.client.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class OrderTools {

    @Tool("验证订单ID是否有效，ID需以'ORD'开头")
    public String validateOrder(String orderId) {
        System.out.println("验证订单: " + orderId);
        if (orderId != null && orderId.startsWith("ORD")) {
            return "订单 " + orderId + " 有效。";
        }
        return "订单 " + orderId + " 无效。";
    }

    @Tool("为指定订单预留库存，如果订单ID是ORD001则成功，否则失败")
    public String reserveInventory(String orderId) {
        System.out.println("预留库存: " + orderId);
        if ("ORD001".equals(orderId)) {
            return "库存预留成功。";
        }
        return "库存预留失败，库存不足。";
    }

    @Tool("为指定订单扣款")
    public String chargePayment(String orderId) {
        System.out.println("扣款: " + orderId);
        return "扣款成功。";
    }

    @Tool("发送订单确认邮件")
    public String sendConfirmationEmail(String orderId) {
        System.out.println("发送邮件: " + orderId);
        return "确认邮件已发送。";
    }

    @Tool("因失败回滚订单")
    public String rollbackOrder(String orderId) {
        System.out.println("回滚订单: " + orderId);
        return "订单 " + orderId + " 已回滚。";
    }
}
