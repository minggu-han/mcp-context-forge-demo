package com.demo.cmp.client;

import com.demo.cmp.client.properties.McpProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class McpDirectTest {

    @Autowired
    private McpProperties mcpProperties;

    @Test
    public void testInitialize() {
        String url = mcpProperties.getStreamableHttpUrl();
        String token = mcpProperties.getJwt();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("method", "initialize");
        request.put("id", 1);
        request.put("params", Map.of(
                "protocolVersion", "2025-03-26",
                "capabilities", Map.of()
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, Map.class
        );

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response: " + response.getBody());
    }

    @Test
    public void testInitialize1() {
        String url = mcpProperties.getStreamableHttpUrl();
        String token = mcpProperties.getJwt();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // 尝试不同的 params 结构
        Map<String, Object> params = new HashMap<>();
        params.put("protocolVersion", "0.1.0");  // 尝试不同版本
        params.put("clientInfo", Map.of(
                "name", "test-client",
                "version", "1.0.0"
        ));
        params.put("capabilities", Map.of());  // 可能是空的但需要存在

        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("method", "initialize");
        request.put("id", 1);
        request.put("params", params);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, Map.class
            );
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFullMcpFlow() {
        String url = mcpProperties.getStreamableHttpUrl();
        String token = mcpProperties.getJwt();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();
        Integer requestId = 1;

        // 1. 初始化
        Map<String, Object> initParams = new HashMap<>();
        initParams.put("protocolVersion", "0.1.0");
        initParams.put("clientInfo", Map.of(
                "name", "test-client",
                "version", "1.0.0"
        ));
        initParams.put("capabilities", Map.of());

        Map<String, Object> initRequest = new HashMap<>();
        initRequest.put("jsonrpc", "2.0");
        initRequest.put("method", "initialize");
        initRequest.put("id", requestId++);
        initRequest.put("params", initParams);

        ResponseEntity<Map> initResponse = restTemplate.exchange(
                url, HttpMethod.POST,
                new HttpEntity<>(initRequest, headers),
                Map.class
        );
        System.out.println("Init Response: " + initResponse.getBody());

        // 2. 发送 initialized 通知（重要！）
        Map<String, Object> initializedNotification = new HashMap<>();
        initializedNotification.put("jsonrpc", "2.0");
        initializedNotification.put("method", "notifications/initialized");
        // 通知没有 id

        restTemplate.exchange(
                url, HttpMethod.POST,
                new HttpEntity<>(initializedNotification, headers),
                Map.class
        );
        System.out.println("Initialized notification sent");

        // 3. 列出工具
        Map<String, Object> listToolsRequest = new HashMap<>();
        listToolsRequest.put("jsonrpc", "2.0");
        listToolsRequest.put("method", "tools/list");
        listToolsRequest.put("id", requestId++);
        listToolsRequest.put("params", Map.of());

        ResponseEntity<Map> toolsResponse = restTemplate.exchange(
                url, HttpMethod.POST,
                new HttpEntity<>(listToolsRequest, headers),
                Map.class
        );
        System.out.println("Tools List: " + toolsResponse.getBody());

        // 4. 调用工具
        Map<String, Object> callToolParams = new HashMap<>();
        callToolParams.put("name", "sendemail");
        callToolParams.put("arguments", Map.of(
                "title", "开会",
                "toList", List.of("20@qq.com"),
                "ccList", List.of("0099@qq.com"),
                "content", "nihao"
        ));

        Map<String, Object> callToolRequest = new HashMap<>();
        callToolRequest.put("jsonrpc", "2.0");
        callToolRequest.put("method", "tools/call");
        callToolRequest.put("id", requestId++);
        callToolRequest.put("params", callToolParams);

        ResponseEntity<Map> callResponse = restTemplate.exchange(
                url, HttpMethod.POST,
                new HttpEntity<>(callToolRequest, headers),
                Map.class
        );
        System.out.println("Tool Call Response: " + callResponse.getBody());
    }

}
