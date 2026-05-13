package com.demo.cmp.client.agent.config.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class ChatAgentToolProviderConfig {

    public static final String STREAMABLE_HTTP_URL = "http://localhost:8080/servers/17ad193a83d8442daf885f50fbf3dea1/mcp";

    public static final String HEADER_KEY = "Authorization";

    public static final String HEADER_VALUE = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluQGV4YW1wbGUuY29tIiwiaWF0IjoxNzc4NTc2Mjk0LCJpc3MiOiJtY3BnYXRld2F5IiwiYXVkIjoibWNwZ2F0ZXdheS1hcGkiLCJqdGkiOiI5ZWU0YTM3OS03ZjI3LTRiN2QtOTljYi01MzQ0MjkwMWNkMjYiLCJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImV4cCI6NDkwMDY0MDI5NH0.o_-OE0gK44n7hQCNUWy21VOqhSuTzHRoutM3VY8yq-U";


    @Bean("chatAgentToolProvider")
    public ToolProvider chatAgentToolProvider() {
        StreamableHttpMcpTransport transport = new StreamableHttpMcpTransport.Builder()
                .url(STREAMABLE_HTTP_URL)
                .customHeaders(Map.of(HEADER_KEY, HEADER_VALUE))
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
