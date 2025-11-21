package com.haeil.full.contract.controller;

import com.haeil.full.cases.dto.response.CaseInfoResponse;
import com.haeil.full.cases.service.CasesService;
import com.haeil.full.contract.dto.request.ContractCreateRequest;
import com.haeil.full.contract.dto.response.ContractDetailResponse;
import com.haeil.full.contract.dto.response.ContractItemResponse;
import com.haeil.full.contract.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final CasesService casesService;

    @PostMapping
    public String createContract(
            @Valid @ModelAttribute ContractCreateRequest request, Model model) {
        ContractDetailResponse response = contractService.createContract(request);
        model.addAttribute("contract", response);
        return "contract/detail";
    }

    @GetMapping
    public String getContract(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContractItemResponse> contractItemResponses =
                contractService.getContractList(pageable);
        model.addAttribute("contracts", contractItemResponses);
        return "contract/list";
    }

    @GetMapping("/info/{caseId}")
    public String getCaseInfo(@PathVariable Long caseId, Model model) {
        CaseInfoResponse response = casesService.getCaseInfo(caseId);
        model.addAttribute("caseInfo", response);
        return "contract/caseInfo";
    }

    @GetMapping("/{contractId}")
    public String getContractById(@PathVariable Long contractId, Model model) {
        ContractDetailResponse response = contractService.getContractDetail(contractId);
        model.addAttribute("contract", response);
        return "contract/detail";
    }

    @GetMapping("/new")
    public String newContractForm(Model model) {
        return "contract/form";
    }
}
