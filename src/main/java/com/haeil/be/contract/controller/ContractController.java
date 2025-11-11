package com.haeil.be.contract.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Contract", description = "수임 관련 API")
@RequestMapping("/api/v1/contract")
@RestController
@RequiredArgsConstructor
public class ContractController {
}
