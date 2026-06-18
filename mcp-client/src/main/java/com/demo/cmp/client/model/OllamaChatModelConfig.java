package com.demo.cmp.client.model;

import com.demo.cmp.client.properties.OllamaProperties;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OllamaChatModelConfig {

    @Autowired
    private OllamaProperties ollamaProperties;

    @Bean("ollamaChatModel")
    public ChatModel ollamaChatModel() {
        ChatModel model = OllamaChatModel.builder()
                .baseUrl(ollamaProperties.getBaseUrl())
                .modelName(ollamaProperties.getModelName())
                .logRequests(true)
                .logResponses(true)
                .think(false)
                .build();
        return model;
    }

}
