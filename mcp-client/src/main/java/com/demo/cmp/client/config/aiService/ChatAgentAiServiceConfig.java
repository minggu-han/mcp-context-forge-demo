package com.demo.cmp.client.config.aiService;

import com.demo.cmp.client.agent.ChatAgent;
import com.demo.cmp.client.factory.ToolProviderFactory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAgentAiServiceConfig {

    @Bean
    public ChatAgent chatAgent(/*ChatModel ollamaChatModel,*/
                               ToolProviderFactory toolProviderFactory,
                               ChatMemoryProvider chatAgentChatMemoryProvider,
                               ChatModel qwenChatModel) {
        return AiServices.builder(ChatAgent.class)
//                .chatModel(ollamaChatModel)
                .chatModel(qwenChatModel)
                .toolProviders(toolProviderFactory.getToolProviderList())
                .chatMemoryProvider(chatAgentChatMemoryProvider)
                .build();
    }

}
