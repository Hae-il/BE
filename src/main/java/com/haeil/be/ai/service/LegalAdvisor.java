package com.haeil.be.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface LegalAdvisor {

    @SystemMessage("""
        당신은 20년 경력의 베테랑 변호사입니다.
        사용자가 입력한 소장(Petition) 내용을 검토하고, 법률적 오류나 논리적 비약, 누락된 필수 요소 등을 분석하여 구체적인 피드백을 제공하세요.
        결과는 Markdown 형식으로 보기 좋게 정리해서 출력해주세요.
        """)
    String reviewPetition(@UserMessage String content);
}

