package com.haeil.full.contract.controller;

import com.haeil.full.cases.dto.response.CaseInfoResponse;
import com.haeil.full.cases.service.CasesService;
import com.haeil.full.contract.domain.type.ContractStatus;
import com.haeil.full.contract.dto.request.ContractCreateRequest;
import com.haeil.full.contract.dto.request.ContractStatusUpdateRequest;
import com.haeil.full.contract.dto.response.ContractDetailResponse;
import com.haeil.full.contract.dto.response.ContractItemResponse;
import com.haeil.full.contract.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final CasesService casesService;

    @PostMapping
    public String createContract(
            @Valid @ModelAttribute ContractCreateRequest request, Model model) {
        contractService.createContract(request);
        return "redirect:/contracts";
    }

    @GetMapping
    public String getContract(
            @RequestParam(required = false) ContractStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContractItemResponse> contractItemResponses =
                contractService.getContractList(status, pageable);
        model.addAttribute("contracts", contractItemResponses);
        return "projects/contracts/list";
    }

    @GetMapping("/info/{caseId}")
    public String getCaseInfo(@PathVariable Long caseId, Model model) {
        CaseInfoResponse response = casesService.getCaseInfo(caseId);
        model.addAttribute("caseInfo", response);
        return "projects/contracts/caseInfo";
    }

    @GetMapping("/{contractId}")
    public String getContractById(@PathVariable Long contractId, Model model) {
        ContractDetailResponse response = contractService.getContractDetail(contractId);
        model.addAttribute("contract", response);
        return "projects/contracts/detail";
    }

    @GetMapping("/new")
    public String newContractForm(
            @RequestParam Long caseId, @RequestParam String caseNumber, Model model) {
        model.addAttribute("caseId", caseId);
        model.addAttribute("caseNumber", caseNumber);
        return "projects/contracts/form";
    }

    @PatchMapping("/{contractId}/status")
    @ResponseBody
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long contractId, @RequestBody ContractStatusUpdateRequest request) {
        contractService.updateStatus(contractId, request);
        return ResponseEntity.ok().build();
    }
}
