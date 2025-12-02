package com.haeil.full.calendar.adapter;

import com.haeil.full.calendar.domain.CalendarEvent;
import com.haeil.full.cases.domain.CaseEvent;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.repository.CaseEventRepository;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaseEventAdapter implements CalendarEventAdapter {

    private final CaseEventRepository caseEventRepository;

    @Override
    public List<CalendarEvent> getEvents(LocalDateTime start, LocalDateTime end, User user) {
        if (user == null) {
            return List.of();
        }

        List<CaseEvent> caseEvents =
                caseEventRepository.findAllByDateBetweenAndAttorney(start, end, user);

        return caseEvents.stream().map(this::toCalendarEvent).collect(Collectors.toList());
    }

    private CalendarEvent toCalendarEvent(CaseEvent caseEvent) {
        String caseTitle =
                caseEvent.getCases() != null ? caseEvent.getCases().getTitle() : "Unknown Case";
        String eventTypeLabel =
                caseEvent.getType() != null ? caseEvent.getType().getLabel() : "기일";
        String title = String.format("[%s] %s", eventTypeLabel, caseTitle);

        Long caseId = null;
        String caseStatus = null;

        if (caseEvent.getCases() != null) {
            caseId = caseEvent.getCases().getId();
            CaseStatus status = caseEvent.getCases().getCaseStatus();
            if (status != null) {
                caseStatus = status.name(); // ONGOING, COMPLETED, etc.
            }
        }

        Map<String, Object> props = new HashMap<>();
        if (caseId != null) {
            props.put("caseId", caseId);
        }
        if (caseStatus != null) {
            props.put("status", caseStatus);
        }

        return CalendarEvent.builder()
                .id("case_" + caseEvent.getId())
                .title(title)
                .start(caseEvent.getDate())
                // 기일은 보통 1시간 정도로 가정하거나, 별도 종료 시간이 있다면 사용
                .end(caseEvent.getDate().plusHours(1))
                .className("bg-danger-subtle text-danger") // 사건 기일은 붉은색 계열로 표시
                .type("CASE_EVENT")
                .allDay(false)
                .extendedProps(props)
                .build();
    }
}
