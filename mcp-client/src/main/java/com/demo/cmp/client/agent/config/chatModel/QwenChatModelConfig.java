//package com.demo.cmp.client.agent.config.chatModel;
//
//import com.demo.cmp.client.agent.config.QwenProperties;
//import dev.langchain4j.model.chat.ChatModel;
//import dev.langchain4j.model.dashscope.QwenChatModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class QwenChatModelConfig {
//
//    @Autowired
//    private QwenProperties qwenProperties;
//
//    @Bean("qwenChatModel")
//    public ChatModel qwenChatModel() {
//        ChatModel model = QwenChatModel.builder()
//                .apiKey(qwenProperties.getApiKey())
//                .modelName(qwenProperties.getModelName())
//                .logRequests(true)
//                .logResponses(true)
//                .build();
//        return model;
//    }
//
//}
