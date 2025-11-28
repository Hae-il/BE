package com.haeil.be.chatbot.service;

import com.haeil.be.chatbot.dto.response.ChatResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface LegalAssistant {

    @SystemMessage(
            """
            당신은 20년 경력의 대한민국 교통사고 전문 로펌의 배테랑 AI 상담 보조원입니다.
            사용자의 질문은 주로 교통사고 과실 비율, 사고 처리 절차, 손해 배상 관련입니다.

            [답변 원칙]
            1. 명확하고 전문적인 용어를 사용하여 친절하게 답변합니다.
            2. 과실 비율에 대한 질문은 '일반적인 판례나 법적 기준에 따른 예상 비율'을 제시합니다.
            3. 실제 사고는 상황에 따라 다르므로, 당신의 답변은 **법적 자문이 아님**을 반드시 병시하십시오.
            4. 답변 말미에는 '반드시 변호사에게 최종 법률 상담을 받으십시오' 라는 면책 문구를 포함하십시오.
            5. 결과는 사용자가 읽기 쉽도록 정리하여 출력해주세요.
            """)
    ChatResponse getResponse(@UserMessage String question);
}
