package com.demo.cmp.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class McpClientService {

    private final String url;
    private final HttpHeaders headers;
    private final RestTemplate restTemplate;
    private final AtomicInteger requestId;
    private final ObjectMapper objectMapper;

    public McpClientService(String url, String jwt) {
        this.url = url;
        this.headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        this.restTemplate = new RestTemplate();
        this.requestId = new AtomicInteger(1);
        this.objectMapper = new ObjectMapper();
    }

    public void initialize() {
        Map<String, Object> params = new HashMap<>();
        params.put("protocolVersion", "0.1.0");
        params.put("clientInfo", Map.of("name", "mcp-client", "version", "1.0.0"));
        params.put("capabilities", Map.of());

        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("method", "initialize");
        request.put("id", nextId());
        request.put("params", params);

        ResponseEntity<Map> response = post(request);
        System.out.println("MCP Init Response: " + response.getBody());

        // 发送 initialized 通知
        Map<String, Object> notification = new HashMap<>();
        notification.put("jsonrpc", "2.0");
        notification.put("method", "notifications/initialized");

        post(notification);
        System.out.println("MCP Initialized notification sent");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> listTools() {
        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("method", "tools/list");
        request.put("id", nextId());
        request.put("params", Map.of());

        ResponseEntity<Map> response = post(request);
        Map<String, Object> body = response.getBody();
        System.out.println(body);
        Map<String, Object> result = (Map<String, Object>) body.get("result");
        return (List<Map<String, Object>>) result.get("tools");
    }

    @SuppressWarnings("unchecked")
    public String callTool(String toolName, String argumentsJson) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", toolName);
        params.put("arguments", parseJsonToMap(argumentsJson));

        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("method", "tools/call");
        request.put("id", nextId());
        request.put("params", params);

        ResponseEntity<Map> response = post(request);
        Map<String, Object> body = response.getBody();
        Map<String, Object> result = (Map<String, Object>) body.get("result");

        if (result != null && result.get("content") != null) {
            List<Map<String, Object>> content = (List<Map<String, Object>>) result.get("content");
            if (!content.isEmpty()) {
                return content.get(0).get("text").toString();
            }
        }
        return body.toString();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonToMap(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            return Map.of();
        }
    }

    private int nextId() {
        return requestId.getAndIncrement();
    }

    private ResponseEntity<Map> post(Map<String, Object> body) {
        return restTemplate.exchange(
                url, HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );
    }
}