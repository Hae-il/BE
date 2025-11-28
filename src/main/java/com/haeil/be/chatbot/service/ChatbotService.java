package com.haeil.be.chatbot.service;

import com.haeil.be.chatbot.dto.response.ChatResponse;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
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

    public ChatResponse getResponse(Long sessionId, String question) {

        String llmResponse = legalAssistant.getAdvice(sessionId, question);
        ChatMemory sessionMemory = chatMemoryProvider.get(sessionId);

        List<ChatMessage> messages = sessionMemory.messages();
        int totalMessages = messages.size();
        long chatCountLong =
                messages.stream()
                        .filter(
                                message ->
                                        message.type()
                                                .equals(
                                                        dev.langchain4j.data.message.ChatMessageType
                                                                .USER))
                        .count();
        int chatCount = (int) chatCountLong;

        log.info(
                "--- [챗봇 응답] --- 세션 ID: {}, 누적 메시지 수: {}, 사용자 질문 횟수: {}",
                sessionId,
                totalMessages,
                chatCount);

        String finalAnswer = llmResponse;

        // 5회 이상 질의 시 예약 유도
        if (chatCount >= 5) {
            String memorySummary = getMemorySummary(messages);

            String reservationGuidance = "\n\n---\n";
            reservationGuidance += "## 📢 전문 상담 예약 안내\n";
            reservationGuidance +=
                    "총 **"
                            + chatCount
                            + "회**의 심층 상담을 진행하셨습니다. "
                            + "현재 논의된 내용을 바탕으로 변호사와 직접 상담하시는 것을 권유합니다.";
            reservationGuidance += "\n\n**[최근 상담 내용 요약]**\n";
            reservationGuidance += memorySummary;
            reservationGuidance += "\n\n상담 예약을 원하시면 '예약'이라고 말씀해주세요.";

            finalAnswer += reservationGuidance;
        }
        return new ChatResponse(finalAnswer);
    }

    private String getMemorySummary(List<ChatMessage> messages) {
        StringBuilder summary = new StringBuilder();

        int start = Math.max(0, messages.size() - 6);

        for (int i = start; i < messages.size(); i++) {
            ChatMessage message = messages.get(i);
            String messageString = message.toString();
            String role;

            if (messageString.startsWith("User")) {
                role = "Q";
            } else if (messageString.startsWith("Ai")) {
                role = "A";
            } else {
                role = "M";
            }
            String textContent = message.text().replaceAll("\n", " ");
            summary.append("* **").append(role).append("**: ").append(textContent).append("\n");
        }
        return summary.toString();
    }
}
