package com.haeil.full.calendar.adapter;

import com.haeil.full.calendar.domain.CalendarEvent;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventAdapter {
    List<CalendarEvent> getEvents(LocalDateTime start, LocalDateTime end, User user);
}
