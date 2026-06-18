package com.demo.cmp.client.mcp;

import com.demo.cmp.client.service.McpClientService;
import com.demo.cmp.client.properties.McpProperties;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.service.tool.ToolProviderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(
        name = "mcp.streamableHttp-enable",           // 配置项的key
        havingValue = "true",           // 期望的值（默认为空）
        matchIfMissing = false          // 如果配置不存在时是否匹配（默认false）
)
public class StreamableHttpToolProviderConfig {

    @Autowired
    private McpProperties mcpProperties;

    @Bean("streamableHttpToolProvider")
    public ToolProvider streamableHttpToolProvider() {
        McpClientService mcpClient = new McpClientService(
                mcpProperties.getStreamableHttpUrl(),
                mcpProperties.getJwt()
        );

        mcpClient.initialize();

        Map<ToolSpecification, ToolExecutor> toolMap = new LinkedHashMap<>();
        for (Map<String, Object> tool : mcpClient.listTools()) {
            String name = (String) tool.get("name");
            String description = (String) tool.get("description");

            @SuppressWarnings("unchecked")
            Map<String, Object> inputSchema = (Map<String, Object>) tool.get("inputSchema");

            @SuppressWarnings("unchecked")
            Map<String, Object> annotations = (Map<String, Object>) tool.getOrDefault("annotations", Map.of());

            ToolSpecification spec = ToolSpecification.builder()
                    .name(name)
                    .description(description)
                    .parameters(buildJsonObjectSchema(inputSchema))
                    .metadata(annotations)
                    .build();

            ToolExecutor executor = (request, memoryId) ->
                    mcpClient.callTool(name, request.arguments());

            toolMap.put(spec, executor);
        }

        return request -> new ToolProviderResult(toolMap);
    }

    @SuppressWarnings("unchecked")
    private JsonObjectSchema buildJsonObjectSchema(Map<String, Object> schema) {
        if (schema == null || schema.isEmpty()) {
            return JsonObjectSchema.builder().build();
        }

        Map<String, JsonSchemaElement> properties = new LinkedHashMap<>();
        Map<String, Object> props = (Map<String, Object>) schema.get("properties");
        if (props != null) {
            for (Map.Entry<String, Object> entry : props.entrySet()) {
                Map<String, Object> propSchema = (Map<String, Object>) entry.getValue();
                properties.put(entry.getKey(), buildSchemaElement(propSchema));
            }
        }

        List<String> required = new ArrayList<>();
        Object requiredObj = schema.get("required");
        if (requiredObj instanceof List) {
            required = ((List<Object>) requiredObj).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return JsonObjectSchema.builder()
                .addProperties(properties)
                .required(required)
                .build();
    }

    @SuppressWarnings("unchecked")
    private JsonSchemaElement buildSchemaElement(Map<String, Object> propSchema) {
        String type = (String) propSchema.getOrDefault("type", "string");
        String desc = (String) propSchema.get("description");

        return switch (type) {
            case "string" -> {
                var b = JsonStringSchema.builder();
                if (desc != null) b.description(desc);
                yield b.build();
            }
            case "number" -> {
                var b = JsonNumberSchema.builder();
                if (desc != null) b.description(desc);
                yield b.build();
            }
            case "integer" -> {
                var b = JsonIntegerSchema.builder();
                if (desc != null) b.description(desc);
                yield b.build();
            }
            case "boolean" -> {
                var b = JsonBooleanSchema.builder();
                if (desc != null) b.description(desc);
                yield b.build();
            }
            case "array" -> {
                var b = JsonArraySchema.builder();
                if (desc != null) b.description(desc);
                Map<String, Object> items = (Map<String, Object>) propSchema.get("items");
                if (items != null) {
                    b.items(buildSchemaElement(items));
                }
                yield b.build();
            }
            case "object" -> {
                yield buildJsonObjectSchema(propSchema);
            }
            default -> {
                var b = JsonStringSchema.builder();
                if (desc != null) b.description(desc);
                yield b.build();
            }
        };
    }
}