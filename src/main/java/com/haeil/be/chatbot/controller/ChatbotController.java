package com.haeil.be.chatbot.controller;

import com.haeil.be.chatbot.dto.request.ChatRequest;
import com.haeil.be.chatbot.dto.response.ChatResponse;
import com.haeil.be.chatbot.service.ChatbotService;
import com.haeil.be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatbot")
@Tag(name = "Chatbot", description = "챗봇 관련 API")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<Object>> askQuestion(@RequestBody ChatRequest request){
        ChatResponse response = chatbotService.getResponse(request.question());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(response));
    }

}
