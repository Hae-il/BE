package com.haeil.be.contract.controller;

import com.haeil.be.contract.dto.request.ContractCreateRequest;
import com.haeil.be.contract.dto.response.ContractItemResponse;
import com.haeil.be.contract.dto.response.ContractResponse;
import com.haeil.be.contract.service.ContractService;
import com.haeil.be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Contract", description = "수임 관련 API")
@RequestMapping("/api/v1/contract")
@RestController
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @Operation(summary = "수임 계약 생성 API", description = "수임 계약을 생성합니다.")
    @PostMapping()
    public ResponseEntity<ApiResponse<Object>> createContract(@Valid @RequestBody ContractCreateRequest request){
        ContractResponse response = contractService.createContract(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(response));
    }

    @Operation(summary = "수임 목록 조회 API", description = "수임 계약 리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getContract(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size){
        // 추후에 @AuthenticationalPrincipal 사용 예정
        Pageable pageable = PageRequest.of(page, size);
        Page<ContractItemResponse> contractItemResponses = contractService.getContractList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(contractItemResponses));
    }
}
