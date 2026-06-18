package com.demo.cmp.client.agent;

import com.demo.cmp.client.factory.ToolProviderFactory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAgentAiServiceConfig {

    @Bean
    public ChatAgent chatAgent(ToolProviderFactory toolProviderFactory
                                , ChatMemoryProvider chatAgentChatMemoryProvider
//                               , ChatModel ollamaChatModel
                                , ChatModel deepseekChatModel
//                                , ChatModel qwenChatModel
    ) {
        return AiServices.builder(ChatAgent.class)
//                .chatModel(ollamaChatModel)
                .chatModel(deepseekChatModel)
//                .chatModel(qwenChatModel)
                .toolProviders(toolProviderFactory.getToolProviderList())
                .chatMemoryProvider(chatAgentChatMemoryProvider)
                .build();
    }

}
