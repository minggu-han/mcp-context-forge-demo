package com.demo.cmp.client.config.mcp;

import com.demo.cmp.client.properties.McpProperties;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChatAgentToolProviderConfig {

    public static final String STREAMABLE_HTTP_URL = "http://localhost:8080/servers/17ad193a83d8442daf885f50fbf3dea1/mcp";

    public static final String HEADER_KEY = "Authorization";

    public static final String HEADER_VALUE = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluQGV4YW1wbGUuY29tIiwiaWF0IjoxNzc4NTc2Mjk0LCJpc3MiOiJtY3BnYXRld2F5IiwiYXVkIjoibWNwZ2F0ZXdheS1hcGkiLCJqdGkiOiI5ZWU0YTM3OS03ZjI3LTRiN2QtOTljYi01MzQ0MjkwMWNkMjYiLCJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImV4cCI6NDkwMDY0MDI5NH0.o_-OE0gK44n7hQCNUWy21VOqhSuTzHRoutM3VY8yq-U";


    @Autowired
    private McpProperties mcpProperties;

    @Bean("chatAgentToolProvider")
    public ToolProvider chatAgentToolProvider() {
/*
{
    "userNo":"60067710",
    "userMessage": "帮我发个邮件，主旨 智能水表会议  发给snowhan@msi.com 抄送给angdy@msi.com  内容就说今天下午5点开会"
}
 */
        // 使用基本认证
        String auth = "admin@example.com:uLnFA-NW9J:22_g";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + mcpProperties.getJwt());
//        headers.put("Authorization", "Basic " + encodedAuth);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        StreamableHttpMcpTransport transport = new StreamableHttpMcpTransport.Builder()
                .url(mcpProperties.getUrl())
                .customHeaders(headers)
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        List<ToolSpecification> toolSpecifications = mcpClient.listTools();
        for (ToolSpecification toolSpecification : toolSpecifications) {
            System.out.println(toolSpecification);
        }

        return McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
    }
}
