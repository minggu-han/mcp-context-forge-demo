package com.demo.cmp.client.momery;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatAgentChatMemoryConfig {

    /**
     * 为每个用户分配独立的ChatMemoory
     */
    @Bean("chatAgentChatMemoryProvider")
    public ChatMemoryProvider chatAgentChatMemoryProvider(RedisChatMemoryStore redisChatMemoryStore) {
        //并发场景下
        ConcurrentHashMap<Object, ChatMemory> memories = new ConcurrentHashMap<>();
        ChatMemoryProvider chatMemoryProvider = (ChatMemoryProvider)memoryId ->
                memories.computeIfAbsent(memoryId, id ->
                                MessageWindowChatMemory.builder()
                                        .id(memoryId)           // 给记忆设置一个ID（可选，便于调试）
                                        .chatMemoryStore(redisChatMemoryStore)
                                        .maxMessages(20)        // 每个记忆最多保留10条消息
                                        .build()
                        );
        return chatMemoryProvider;
    }

    //持久化记忆 ChatMemoryStore接口实现




}
