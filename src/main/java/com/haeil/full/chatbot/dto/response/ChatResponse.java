package com.haeil.full.chatbot.dto.response;

import java.util.List;

public record ChatResponse(String answer, List<ChatHistoryItem> history, String action) {
    public ChatResponse(String answer) {
        this(answer, null, "TEXT");
    }

    public ChatResponse(String answer, List<ChatHistoryItem> history) {
        this(answer, history, "TEXT");
    }

    public ChatResponse(String answer, String action) {
        this(answer, null, action);
    }
}
