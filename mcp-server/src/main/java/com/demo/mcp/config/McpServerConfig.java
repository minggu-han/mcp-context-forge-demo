package com.demo.mcp.config;

import com.demo.mcp.service.DemoTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(DemoTools demoTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(demoTools)
                .build();
    }
}