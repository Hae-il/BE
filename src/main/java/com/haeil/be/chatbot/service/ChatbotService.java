package com.haeil.be.chatbot.service;

import com.haeil.be.chatbot.dto.response.ChatResponse;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
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

    private static final int MAX_FREE_QUESTIONS = 5;

    public ChatResponse getResponse(Long sessionId, String question) {

        // 1. 현재 세션의 메모리 로드
        ChatMemory sessionMemory = chatMemoryProvider.get(sessionId);
        List<ChatMessage> messages = sessionMemory.messages();

        // 2. 이전까지의 사용자 질문 횟수 계산
        long previousQuestionCount =
                messages.stream()
                        .filter(message -> message.type().equals(ChatMessageType.USER))
                        .count();

        log.info(
                "--- [챗봇 요청] --- 세션 ID: {}, 현재까지 질문 횟수: {}",
                sessionId,
                previousQuestionCount);

        // 3. 6번째 질문(이미 5회 완료)부터는 답변 거부 및 예약 안내
        if (previousQuestionCount >= MAX_FREE_QUESTIONS) {
            String memorySummary = getMemorySummary(messages);

            StringBuilder reservationGuidance = new StringBuilder();
            reservationGuidance.append("<b>무료 상담 횟수(").append(MAX_FREE_QUESTIONS).append("회)를 모두 소진하셨습니다.</b><br>");
            reservationGuidance.append("더 깊은 상담이나 구체적인 법률 조언이 필요하시다면, 변호사님께 상담 예약을 신청해주세요.<br><br>");

            reservationGuidance.append("<h3>📝 [지금까지의 상담 요약]</h3>");
            reservationGuidance.append(memorySummary);

            reservationGuidance.append("<br><br>👉 <b>상담 예약을 원하시면 '예약'이라고 말씀해주세요.</b>");

            return new ChatResponse(reservationGuidance.toString());
        }

        // 4. 5회 이내라면 정상적으로 AI 응답 생성
        String llmResponse = legalAssistant.getAdvice(sessionId, question);

        return new ChatResponse(llmResponse);
    }

    private String getMemorySummary(List<ChatMessage> messages) {
        StringBuilder summary = new StringBuilder();
        String lastQuestion = null;

        // 전체 대화 내용을 순회하며 질문과 답변을 짝지어 요약
        for (ChatMessage message : messages) {
            // 줄바꿈을 <br> 태그로 변환하여 HTML에서 줄바꿈 적용되도록 함
            String textContent = message.text().replaceAll("\n", "<br>");

            if (message.type() == ChatMessageType.USER) {
                lastQuestion = textContent;
            } else if (message.type() == ChatMessageType.AI) {
                if (lastQuestion != null) {
                    // <details>와 <summary> 태그를 사용하여 아코디언 UI 적용
                    summary.append("<details style='margin-bottom: 10px; border: 1px solid #ddd; border-radius: 5px; padding: 5px;'>");
                    summary.append("<summary style='cursor: pointer; font-weight: bold; padding: 5px;'>Q. ")
                            .append(lastQuestion)
                            .append("</summary>");
                    summary.append("<div style='margin-top: 5px; padding: 10px; background-color: #f9f9f9; border-top: 1px solid #ddd;'>A. ")
                            .append(textContent)
                            .append("</div>");
                    summary.append("</details>");
                    
                    lastQuestion = null; // 질문 처리 완료
                }
            }
        }
        return summary.toString();
    }
}
