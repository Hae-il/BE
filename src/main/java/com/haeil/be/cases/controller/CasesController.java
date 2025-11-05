package com.haeil.be.cases.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Cases", description="사건 관련 API")
@RequestMapping("/api/v1/cases")
@RestController
@RequiredArgsConstructor
public class CasesController {
}
