package com.haeil.be.chatbot.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ScheduleAgent {

    @SystemMessage(
            """
            당신은 의뢰인 일정 비서입니다. 의뢰인의 ID는 {clientId}입니다.
            사용자의 질문을 해결하기 위해 Tool을 호출해야 할 경우, 반드시 이 ID를 사용하십시오.
            Tool 결과를 자연스러운 한국어로 설명하세요.
            """)
    String chat(@V("clientId") Long clientId, @UserMessage String userQuery);
}
