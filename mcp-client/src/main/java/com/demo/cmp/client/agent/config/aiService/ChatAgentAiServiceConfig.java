package com.demo.cmp.client.agent.config.aiService;

import com.demo.cmp.client.agent.ChatAgent;
import com.demo.cmp.client.agent.config.DeepseekProperties;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAgentAiServiceConfig {

    @Bean
    public ChatAgent chatAgent(/*ChatModel ollamaChatModel,*/
                               ToolProvider chatAgentToolProvider,
                               ChatMemoryProvider chatAgentChatMemoryProvider,
                               ChatModel qwenChatModel) {
        return AiServices.builder(ChatAgent.class)
//                .chatModel(ollamaChatModel)
                .chatModel(qwenChatModel)
                .toolProvider(chatAgentToolProvider)
                .chatMemoryProvider(chatAgentChatMemoryProvider)
                .build();
    }

}
