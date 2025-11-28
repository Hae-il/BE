package com.haeil.be.chatbot.dto.response;

import java.util.List;

public record ChatResponse(String answer, List<ChatHistoryItem> history) {
    public ChatResponse(String answer) {
        this(answer, null);
    }
}
