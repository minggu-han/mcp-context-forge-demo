package com.demo.cmp.client.config.chatModel;

import com.demo.cmp.client.config.DeepseekProperties;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class DeepseekChatModelConfig {

    @Autowired
    private DeepseekProperties deepseekProperties;

    @Bean("deepseekChatModel")
    public ChatModel deepseekChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(deepseekProperties.getApiKey())
                .baseUrl(deepseekProperties.getBaseUrl())
                .modelName(deepseekProperties.getModelName())
                .temperature(1.0)
                .maxTokens(2000)
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

}
