package com.demo.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
client_credentials
Issuer URL--->    http://localhost:9000
Token URL--->   http://localhost:9000/oauth2/token
 */
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}