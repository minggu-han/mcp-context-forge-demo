package com.demo.cmp.client.model;

import com.demo.cmp.client.properties.QwenProperties;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class QwenChatModelConfig {

    @Autowired
    private QwenProperties qwenProperties;

    @Bean("qwenChatModel")
    public QwenChatModel qwenChatModel() {
        QwenChatModel chatModel = QwenChatModel.builder()
                .apiKey(qwenProperties.getApiKey())
                .modelName(qwenProperties.getModelName())
                .build();

        return chatModel;
    }

}
