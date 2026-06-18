package com.demo.cmp.client.agent;

import com.demo.cmp.client.factory.ToolProviderFactory;
import com.demo.cmp.client.tools.OrderTools;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.skills.ClassPathSkillLoader;
import dev.langchain4j.skills.Skills;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAgentAiServiceConfig {

    @Bean
    public Skills skills() {
        // 从 classpath 加载所有技能
        return Skills.from(ClassPathSkillLoader.loadSkills("skills"));
    }

    @Bean
    public ChatAgent chatAgent(ToolProviderFactory toolProviderFactory
                                , ChatMemoryProvider chatAgentChatMemoryProvider
                                , Skills skills
                                , OrderTools orderTools
//                               , ChatModel ollamaChatModel
                                , ChatModel deepseekChatModel
//                                , ChatModel qwenChatModel
    ) {
        return AiServices.builder(ChatAgent.class)
//                .chatModel(ollamaChatModel)
                .chatModel(deepseekChatModel)
//                .chatModel(qwenChatModel)
//                .toolProviders(toolProviderFactory.getToolProviderList())
                .tools(orderTools)
                .toolProvider(skills.toolProvider())
                .chatMemoryProvider(chatAgentChatMemoryProvider)
                // 重要：在系统消息中告知 AI 有哪些技能可用
                .systemMessage("""
                        你是一个订单处理助手。
                        你可以使用以下技能：
                        %s
                        当用户请求与某个技能相关时，请先使用 `activate_skill` 工具激活它，然后严格按照技能中的步骤执行。
                        """.formatted(skills.formatAvailableSkills()))
                .build();
    }

}
