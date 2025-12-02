package com.haeil.be.chatbot.service;

import static com.haeil.be.user.exception.errorcode.UserErrorCode.CLIENT_ONLY;
import static com.haeil.be.user.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

import com.haeil.be.cases.domain.CaseEvent;
import com.haeil.be.cases.repository.CaseEventRepository;
import com.haeil.be.chatbot.context.ClientContextHolder;
import com.haeil.be.client.repository.ClientRepository;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.domain.type.Role;
import com.haeil.be.user.exception.UserException;
import com.haeil.be.user.repository.UserRepository;
import dev.langchain4j.agent.tool.Tool;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotScheduleService {

    private final CaseEventRepository caseEventRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Tool("query_client_upcoming_schedules")
    public String getUpcomingSchedulesForClient() {
        Long clientId = ClientContextHolder.getClientId();

        List<CaseEvent> events = caseEventRepository.findUpcomingEventByClientId(clientId);

        if (events == null || events.isEmpty()) {
            return "조회된 다가오는 일정이 없습니다.";
        }

        // LLM이 이해하기 쉬운 단순 문자열으로 변환
        return events.stream()
                .map(
                        event ->
                                String.format(
                                        "사건명: %s, 종류: %s, 날짜: %s, 장소: %s",
                                        event.getCases().getTitle(),
                                        event.getType(),
                                        event.getDate().toString(),
                                        event.getLocation()))
                .collect(Collectors.joining("; "));
    }

    public Long convertToClientId(Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if (user.getRole() != Role.ROLE_CLIENT) {
            throw new UserException(CLIENT_ONLY);
        }

        return clientRepository
                .findByEmail(user.getEmail())
                .map(client -> client.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
