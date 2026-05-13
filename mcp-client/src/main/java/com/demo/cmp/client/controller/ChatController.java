package com.demo.cmp.client.controller;

import com.demo.cmp.client.agent.ChatAgent;
import com.demo.cmp.client.request.ChatRequest;
import com.demo.cmp.client.respose.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private ChatAgent chatAgent;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {
        Result<String> result = chatAgent.chat(chatRequest.getUserNo(), chatRequest.getUserMessage());
        ChatResponse response = new ChatResponse();
        String answer = result.content();
        TokenUsage tokenUsage = result.tokenUsage();
        FinishReason finishReason = result.finishReason();

        response.setAnswer(answer);
        response.setTokenUsage(" 输入:" + tokenUsage.inputTokenCount() +
               ";输出：" + tokenUsage.outputTokenCount() + ";总计:" + tokenUsage.totalTokenCount());
        response.setFinishReason(finishReason);
        return response;
    }

}
