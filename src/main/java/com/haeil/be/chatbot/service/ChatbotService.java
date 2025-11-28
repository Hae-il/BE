package com.haeil.be.chatbot.service;

import com.haeil.be.chatbot.dto.response.ChatResponse;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ChatLanguageModel chatLanguageModel;

    private final LegalAssistant legalAssistant;

    public ChatResponse getResponse(String question) {
        return legalAssistant.getResponse(question);
    }
}
