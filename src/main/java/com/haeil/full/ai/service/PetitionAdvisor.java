package com.haeil.full.ai.service;

import com.haeil.full.cases.dto.response.PetitionResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface PetitionAdvisor {

    @SystemMessage("You are a helpful legal assistant specialized in drafting legal petitions.")
    @UserMessage("{{message}}")
    PetitionResponse generateDraft(String message);
}
