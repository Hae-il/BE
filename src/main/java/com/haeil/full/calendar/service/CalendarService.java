package com.haeil.full.calendar.service;

import com.haeil.full.calendar.adapter.CalendarEventAdapter;
import com.haeil.full.calendar.domain.CalendarEvent;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final List<CalendarEventAdapter> adapters;

    public List<CalendarEvent> getAllEvents(LocalDateTime start, LocalDateTime end, User user) {
        return adapters.stream()
                .map(adapter -> adapter.getEvents(start, end, user))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
