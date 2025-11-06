package com.haeil.be.consultation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Consultation", description = "상담 관련 API")
@RequestMapping("/api/v1/consultation")
@RestController
@RequiredArgsConstructor
public class ConsultationController {}
