package com.haeil.be.settlement.controller;

import com.haeil.be.global.response.ApiResponse;
import com.haeil.be.settlement.dto.request.CreateSettlementRequest;
import com.haeil.be.settlement.dto.request.UpdateSettlementRequest;
import com.haeil.be.settlement.dto.response.SettlementListResponse;
import com.haeil.be.settlement.dto.response.SettlementResponse;
import com.haeil.be.settlement.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Settlement", description = "정산 관련 API")
@RequestMapping("/api/v1/settlements")
@RestController
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @Operation(summary = "정산 생성", description = "새로운 정산 정보를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createSettlement(
            @Valid @RequestBody CreateSettlementRequest request) {
        SettlementResponse response = settlementService.createSettlement(request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "정산 목록 조회", description = "완료된 사건 기준으로 정산 목록을 조회합니다. 정산서가 없는 사건도 포함됩니다.")
    @GetMapping
    public ApiResponse<Object> getSettlements() {
        List<SettlementListResponse> responses = settlementService.getSettlements();
        return ApiResponse.from(responses);
    }

    @Operation(summary = "정산 상세 조회", description = "정산 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<Object> getSettlement(@PathVariable Long id) {
        SettlementResponse response = settlementService.getSettlement(id);
        return ApiResponse.from(response);
    }

    @Operation(summary = "정산 수정", description = "정산 정보를 수정합니다.")
    @PatchMapping("/{id}")
    public ApiResponse<Object> updateSettlement(
            @PathVariable Long id, @Valid @RequestBody UpdateSettlementRequest request) {
        SettlementResponse response = settlementService.updateSettlement(id, request);
        return ApiResponse.from(response);
    }

    // @Operation(summary = "정산 삭제", description = "정산 정보를 삭제합니다.")
    // @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public ApiResponse<Object> deleteSettlement(@PathVariable Long id) {
    //     settlementService.deleteSettlement(id);
    //     return ApiResponse.EMPTY_RESPONSE;
    // }
}
