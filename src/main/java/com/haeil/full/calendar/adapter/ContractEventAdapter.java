package com.haeil.full.calendar.adapter;

import com.haeil.full.calendar.domain.CalendarEvent;
import com.haeil.full.contract.domain.Contract;
import com.haeil.full.contract.repository.ContractRepository;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractEventAdapter implements CalendarEventAdapter {

    private final ContractRepository contractRepository;

    @Override
    public List<CalendarEvent> getEvents(LocalDateTime start, LocalDateTime end, User user) {
        if (user == null) {
            return List.of();
        }

        List<Contract> contracts =
                contractRepository.findAllByDueDateBetweenAndAttorney(
                        start.toLocalDate(), end.toLocalDate(), user);

        return contracts.stream().map(this::toCalendarEvent).collect(Collectors.toList());
    }

    private CalendarEvent toCalendarEvent(Contract contract) {
        String caseTitle =
                contract.getCases() != null ? contract.getCases().getTitle() : "Unknown Case";
        String title = String.format("[계약] %s", caseTitle);
        
        // 계약 기한은 자정으로 설정
        LocalDateTime dueDate = contract.getDueDate().atStartOfDay();

        return CalendarEvent.builder()
                .id("contract_" + contract.getId())
                .title(title)
                .start(dueDate)
                .end(dueDate) // 마감일은 하루 전체 이벤트로 보거나 start와 같게 설정
                .className("bg-warning-subtle text-warning") // 계약은 노란색 계열
                .type("CONTRACT")
                .allDay(true) // 기한은 보통 날짜 단위
                .extendedProps(Map.of("contractId", contract.getId()))
                .build();
    }
}

