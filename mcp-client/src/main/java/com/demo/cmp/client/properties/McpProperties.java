package com.demo.cmp.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mcp")
public class McpProperties {

    private String streamableHttpUrl;

    private String seeUrl;

    private String jwt;

}
