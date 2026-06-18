package com.demo.cmp.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "deepseek")
public class DeepseekProperties {

    private String baseUrl;

    private String apiKey;

    private String modelName;

}
