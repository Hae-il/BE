package com.haeil.full.calendar.controller;

import com.haeil.full.calendar.domain.CalendarEvent;
import com.haeil.full.calendar.service.CalendarService;
import com.haeil.full.user.domain.User;
import com.haeil.full.user.exception.UserException;
import com.haeil.full.user.exception.errorcode.UserErrorCode;
import com.haeil.full.user.repository.UserRepository;
import com.haeil.full.user.service.CustomUserDetails;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserRepository userRepository;

    @GetMapping("/events")
    public ResponseEntity<List<CalendarEvent>> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.ok(List.of());
        }

        User user =
                userRepository
                        .findUserById(userDetails.getId())
                        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<CalendarEvent> events = calendarService.getAllEvents(start, end, user);
        return ResponseEntity.ok(events);
    }
}
