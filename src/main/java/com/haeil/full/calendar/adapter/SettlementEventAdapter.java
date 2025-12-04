package com.haeil.full.calendar.adapter;

import com.haeil.full.calendar.domain.CalendarEvent;
import com.haeil.full.settlement.domain.Settlement;
import com.haeil.full.settlement.repository.SettlementRepository;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementEventAdapter implements CalendarEventAdapter {

    private final SettlementRepository settlementRepository;

    @Override
    public List<CalendarEvent> getEvents(LocalDateTime start, LocalDateTime end, User user) {
        if (user == null) {
            return List.of();
        }

        List<Settlement> settlements =
                settlementRepository.findAllByPaymentDueDateBetweenAndAttorney(
                        start.toLocalDate(), end.toLocalDate(), user);

        return settlements.stream().map(this::toCalendarEvent).collect(Collectors.toList());
    }

    private CalendarEvent toCalendarEvent(Settlement settlement) {
        String caseTitle =
                settlement.getCases() != null ? settlement.getCases().getTitle() : "Unknown Case";
        String title = String.format("[정산] %s 입금기한", caseTitle);

        LocalDateTime dueDate = settlement.getPaymentDueDate().atStartOfDay();

        return CalendarEvent.builder()
                .id("settlement_" + settlement.getId())
                .title(title)
                .start(dueDate)
                .end(dueDate)
                .className("bg-success-subtle text-success") // 정산/입금은 초록색 계열
                .type("SETTLEMENT")
                .allDay(true)
                .extendedProps(Map.of("settlementId", settlement.getId()))
                .build();
    }
}
