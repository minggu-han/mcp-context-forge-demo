package com.demo.cmp.client.agent.config.momery;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ChatAgentChatMemoryConfig {

    @Bean("chatAgentChatMemoryProvider")
    public ChatMemoryProvider chatAgentChatMemoryProvider() {
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)           // 给记忆设置一个ID（可选，便于调试）
                .maxMessages(10)        // 每个记忆最多保留10条消息
                .build();
        return chatMemoryProvider;
    }

}
