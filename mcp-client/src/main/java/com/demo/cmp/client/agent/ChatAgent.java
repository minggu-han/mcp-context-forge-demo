package com.demo.cmp.client.agent;

import dev.langchain4j.service.*;

public interface ChatAgent {

    @SystemMessage("你是一个乐于助人的助手。")
    @UserMessage("请用中文回答：{{user_question}}")
    Result<String> chat(@MemoryId String memoryId, @V("user_question")String userQuestion);

}
