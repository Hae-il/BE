package com.haeil.full.calendar.domain;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarEvent {
    private String id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String className;
    private String type;
    private boolean allDay;
    private Map<String, Object> extendedProps;
}

