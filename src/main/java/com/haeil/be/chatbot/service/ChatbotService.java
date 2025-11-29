package com.haeil.be.chatbot.service;

import com.haeil.be.chatbot.dto.response.ChatHistoryItem;
import com.haeil.be.chatbot.dto.response.ChatResponse;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final LegalAssistant legalAssistant;
    private final ChatMemoryProvider chatMemoryProvider;

    private static final int MAX_FREE_QUESTIONS = 5;

    public ChatResponse getResponse(Long sessionId, String question) {

        ChatMemory sessionMemory = chatMemoryProvider.get(sessionId);
        List<ChatMessage> messages = sessionMemory.messages();

        long previousQuestionCount =
                messages.stream()
                        .filter(message -> message.type().equals(ChatMessageType.USER))
                        .count();

        log.info(
                "--- [챗봇 요청] --- 세션 ID: {}, 현재까지 질문 횟수: {}",
                sessionId,
                previousQuestionCount);

        if (previousQuestionCount >= MAX_FREE_QUESTIONS) {
            List<ChatHistoryItem> history = getChatHistory(messages);

            String reservationGuidance =
                    "무료 상담 횟수("
                            + MAX_FREE_QUESTIONS
                            + "회)를 모두 소진하셨습니다.\n"
                            + "더 깊은 상담이나 구체적인 법률 조언이 필요하시다면, 변호사님께 상담 예약을 신청해주세요.\n\n"
                            + "👉 상담 예약을 원하시면 '예약'이라고 말씀해주세요.";

            return new ChatResponse(reservationGuidance, history);
        }

        String llmResponse = legalAssistant.getAdvice(sessionId, question);

        return new ChatResponse(llmResponse);
    }

    private List<ChatHistoryItem> getChatHistory(List<ChatMessage> messages) {
        List<ChatHistoryItem> history = new ArrayList<>();
        String lastQuestion = null;

        for (ChatMessage message : messages) {
            if (message.type() == ChatMessageType.USER) {
                lastQuestion = message.text();
            } else if (message.type() == ChatMessageType.AI) {
                if (lastQuestion != null) {
                    history.add(new ChatHistoryItem(lastQuestion, message.text()));
                    lastQuestion = null;
                }
            }
        }
        return history;
    }
}
