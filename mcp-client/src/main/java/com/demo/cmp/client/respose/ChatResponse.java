package com.demo.cmp.client.respose;

import dev.langchain4j.model.output.FinishReason;
import lombok.Data;

@Data
public class ChatResponse {
    private String answer;
    private String tokenUsage;
    private FinishReason finishReason;
}
