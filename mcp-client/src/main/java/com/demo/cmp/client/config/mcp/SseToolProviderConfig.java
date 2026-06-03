package com.demo.cmp.client.config.mcp;

import com.demo.cmp.client.properties.McpProperties;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnProperty(
        name = "mcp.sse-enable",           // 配置项的key
        havingValue = "true",           // 期望的值（默认为空）
        matchIfMissing = false          // 如果配置不存在时是否匹配（默认false）
)
public class SseToolProviderConfig {

    @Autowired
    private McpProperties mcpProperties;

    @Bean("sseToolProvider")
    public ToolProvider sseToolProvider() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + mcpProperties.getJwt());

        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl(mcpProperties.getSeeUrl())
                .customHeaders(headers)
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        return McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
    }

}
