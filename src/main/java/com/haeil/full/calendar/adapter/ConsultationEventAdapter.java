package com.haeil.full.calendar.adapter;

import com.haeil.full.calendar.domain.CalendarEvent;
import com.haeil.full.consultation.domain.Consultation;
import com.haeil.full.consultation.repository.ConsultationRepository;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultationEventAdapter implements CalendarEventAdapter {

    private final ConsultationRepository consultationRepository;

    @Override
    public List<CalendarEvent> getEvents(LocalDateTime start, LocalDateTime end, User user) {
        if (user == null) {
            return List.of();
        }

        List<Consultation> consultations =
                consultationRepository.findAllByConsultationDateBetweenAndCounselor(
                        start, end, user);

        return consultations.stream().map(this::toCalendarEvent).collect(Collectors.toList());
    }

    private CalendarEvent toCalendarEvent(Consultation consultation) {
        String clientName =
                consultation.getClient() != null ? consultation.getClient().getName() : "Unknown";
        String title = String.format("[상담] %s님", clientName);

        // 상담 상태에 따른 색상 지정 (예: 예정, 진행중, 완료 등)
        String className = getClassNameByStatus(consultation);

        return CalendarEvent.builder()
                .id("consultation_" + consultation.getId())
                .title(title)
                .start(consultation.getConsultationDate())
                // 상담 시간은 기본 1시간으로 가정하거나, 별도 종료 시간 필드가 있다면 사용
                .end(consultation.getConsultationDate().plusHours(1))
                .className(className)
                .type("CONSULTATION")
                .allDay(false)
                .build();
    }

    private String getClassNameByStatus(Consultation consultation) {
        // Bootstrap 색상 클래스 활용
        // 예: bg-primary, bg-success, bg-warning, bg-danger, bg-info
        return switch (consultation.getStatus()) {
            case IN_PROGRESS -> "bg-primary-subtle text-primary";
            case COMPLETED -> "bg-success-subtle text-success";
            default -> "bg-info-subtle text-info";
        };
    }
}
