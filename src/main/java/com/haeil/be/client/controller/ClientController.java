package com.haeil.be.client.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Client", description="의뢰인 관련 API")
@RequestMapping("/api/v1/client")
@RestController
@RequiredArgsConstructor
public class ClientController {
}
