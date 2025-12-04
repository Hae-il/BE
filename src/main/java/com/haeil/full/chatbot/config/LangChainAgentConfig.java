package com.haeil.full.chatbot.config;

import com.haeil.full.chatbot.service.ChatbotScheduleService;
import com.haeil.full.chatbot.service.ScheduleAgent;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainAgentConfig {

    @Bean
    public ScheduleAgent scheduleAgent(
            ChatLanguageModel chatLanguageModel, ChatbotScheduleService chatbotScheduleService) {
        return AiServices.builder(ScheduleAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(List.of(chatbotScheduleService))
                .build();
    }
}
