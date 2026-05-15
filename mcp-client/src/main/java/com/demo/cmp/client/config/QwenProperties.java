package com.demo.cmp.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qwen")
public class QwenProperties {
    private String apiKey;

    private String modelName;
}
