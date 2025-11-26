package com.haeil.be.ai.service;

import com.haeil.be.ai.dto.response.AiPetitionResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface PetitionAdvisor {

    @SystemMessage("You are a helpful legal assistant specialized in drafting legal petitions.")
    @UserMessage("{{message}}")
    AiPetitionResponse generateDraft(String message);
}
