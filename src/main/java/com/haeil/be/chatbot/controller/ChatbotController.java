package com.haeil.be.chatbot.controller;

import com.haeil.be.chatbot.context.ClientContextHolder;
import com.haeil.be.chatbot.dto.request.ChatAgentRequest;
import com.haeil.be.chatbot.dto.request.ChatRequest;
import com.haeil.be.chatbot.dto.request.ClientContext;
import com.haeil.be.chatbot.dto.request.CreateChatReservationRequest;
import com.haeil.be.chatbot.dto.response.ChatResponse;
import com.haeil.be.chatbot.service.ChatbotScheduleService;
import com.haeil.be.chatbot.service.ChatbotService;
import com.haeil.be.chatbot.service.ScheduleAgent;
import com.haeil.be.global.response.ApiResponse;
import com.haeil.be.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final ScheduleAgent scheduleAgent;
    private final ChatbotScheduleService chatbotScheduleService;

    @Operation(summary = "챗봇 질문 API", description = "챗봇에게 질문을 합니다.")
    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<Object>> askQuestion(@RequestBody ChatRequest request) {
        ChatResponse response = chatbotService.getResponse(request.sessionId(), request.question());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(response));
    }

    @Operation(summary = "챗봇 내 상담 예약 API", description = "챗봇 내에서 상담 예약을 진행합니다.")
    @PostMapping("/reservation")
    public ResponseEntity<ApiResponse<Object>> addReservation(
            @RequestBody CreateChatReservationRequest request) {
        chatbotService.createConsultationReservation(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "챗봇 질문 - Agent 형태 API")
    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse<Object>> askSchedule(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChatAgentRequest request) {
        Long userId = userDetails.getId();
        Long clientId = chatbotScheduleService.convertToClientId(userId);

        ClientContext context = new ClientContext(clientId);
        String userQuery = request.question();

        ClientContextHolder.setClientId(clientId);
        String agentResponse = scheduleAgent.chat(context, userQuery);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(agentResponse));
    }
}
