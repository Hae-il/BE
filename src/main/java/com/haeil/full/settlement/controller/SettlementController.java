package com.haeil.full.settlement.controller;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.exception.CasesException;
import com.haeil.full.cases.exception.errorcode.CasesErrorCode;
import com.haeil.full.cases.repository.CasesRepository;
import com.haeil.full.settlement.domain.type.PaymentStatus;
import com.haeil.full.settlement.domain.type.SettlementStatus;
import com.haeil.full.settlement.dto.request.CreateSettlementRequest;
import com.haeil.full.settlement.dto.request.UpdatePaymentStatusRequest;
import com.haeil.full.settlement.dto.request.UpdateSettlementRequest;
import com.haeil.full.settlement.dto.response.SettlementListResponse;
import com.haeil.full.settlement.dto.response.SettlementResponse;
import com.haeil.full.settlement.service.SettlementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/settlements")
@Controller
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;
    private final CasesRepository casesRepository;

    @GetMapping("/new")
    public String settlementForm(
            @RequestParam(required = false) Long caseId,
            @RequestParam(required = false) String caseNumber,
            Model model) {
        CreateSettlementRequest request = new CreateSettlementRequest();
        if (caseId != null) {
            request.setCaseId(caseId);

            Cases cases =
                    casesRepository
                            .findById(caseId)
                            .orElseThrow(() -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

            model.addAttribute(
                    "clientName", cases.getClient() != null ? cases.getClient().getName() : "-");
            model.addAttribute(
                    "attorneyName",
                    cases.getAttorney() != null ? cases.getAttorney().getName() : "-");
            model.addAttribute(
                    "caseType", cases.getCaseType() != null ? cases.getCaseType().getLabel() : "-");
        }
        model.addAttribute("request", request);
        model.addAttribute("caseId", caseId);
        model.addAttribute("caseNumber", caseNumber);
        return "projects/settlement/form";
    }

    @PostMapping
    public String createSettlement(
            @Valid @ModelAttribute("request") CreateSettlementRequest request, Model model) {
        try {
            SettlementResponse response = settlementService.createSettlement(request);
            if (response.getSettlementStatus() != SettlementStatus.FINAL) {
                return "redirect:/settlements";
            }
            return "redirect:/settlements/" + response.getId() + "?created";
        } catch (Exception e) {
            model.addAttribute("error", "정산 생성 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/settlement/form";
        }
    }

    @GetMapping
    public String getSettlements(
            @RequestParam(required = false) String attorneyName,
            @RequestParam(required = false) PaymentStatus status,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        Page<SettlementListResponse> settlements =
                settlementService.getSettlements(attorneyName, status, pageable);
        model.addAttribute("settlements", settlements);
        return "projects/settlement/list";
    }

    @GetMapping("/{id}")
    public String getSettlement(@PathVariable Long id, Model model) {
        try {
            SettlementResponse response = settlementService.getSettlement(id);
            model.addAttribute("settlement", response);

            if (response.getCaseId() != null) {
                Cases cases =
                        casesRepository
                                .findById(response.getCaseId())
                                .orElseThrow(
                                        () -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

                model.addAttribute(
                        "clientName",
                        cases.getClient() != null ? cases.getClient().getName() : "-");
                model.addAttribute(
                        "attorneyName",
                        cases.getAttorney() != null ? cases.getAttorney().getName() : "-");
                model.addAttribute(
                        "caseType",
                        cases.getCaseType() != null ? cases.getCaseType().getLabel() : "-");
            } else {
                model.addAttribute("clientName", "-");
                model.addAttribute("attorneyName", "-");
                model.addAttribute("caseType", "-");
            }

            model.addAttribute("updateRequest", new UpdateSettlementRequest());
            model.addAttribute("paymentStatusRequest", new UpdatePaymentStatusRequest(null));
            return "projects/settlement/detail";
        } catch (Exception e) {
            model.addAttribute("error", "정산 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/settlement/list";
        }
    }

    @GetMapping("/{id}/edit")
    public String editSettlementForm(@PathVariable Long id, Model model) {
        try {
            SettlementResponse settlement = settlementService.getSettlement(id);

            // 폼 채우기용 Request 객체 생성
            CreateSettlementRequest request = new CreateSettlementRequest();

            if (settlement.getAttorneyFee() != null) {
                request.setAttorneyFee(
                        settlement.getAttorneyFee().setScale(0, java.math.RoundingMode.DOWN));
            }
            if (settlement.getAgreementAmount() != null) {
                request.setAgreementAmount(
                        settlement.getAgreementAmount().setScale(0, java.math.RoundingMode.DOWN));
            }
            if (settlement.getExpenses() != null) {
                request.setExpenses(
                        settlement.getExpenses().setScale(0, java.math.RoundingMode.DOWN));
            }

            request.setIsVatIncluded(settlement.getIsVatIncluded());
            request.setSettlementDate(settlement.getSettlementDate());
            request.setPaymentDueDate(settlement.getPaymentDueDate());
            request.setNote(settlement.getNote());

            Long caseId = settlement.getCaseId();
            request.setCaseId(caseId);

            if (caseId != null) {
                Cases cases =
                        casesRepository
                                .findById(caseId)
                                .orElseThrow(
                                        () -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

                model.addAttribute(
                        "caseNumber",
                        cases.getCaseNumber()); // 주의: Cases의 caseNumber 필드 사용 (또는 C+ID)
                // 하지만 기존 form.html은 caseNumber를 모델 attribute로 받음.
                // Cases 엔티티의 caseNumber 필드는 null일 수 있으므로 C+ID로 통일하는 것이 좋음.
                model.addAttribute("caseNumber", "C" + cases.getId());

                model.addAttribute(
                        "clientName",
                        cases.getClient() != null ? cases.getClient().getName() : "-");
                model.addAttribute(
                        "attorneyName",
                        cases.getAttorney() != null ? cases.getAttorney().getName() : "-");
                model.addAttribute(
                        "caseType",
                        cases.getCaseType() != null ? cases.getCaseType().getLabel() : "-");
            }

            model.addAttribute("request", request);
            model.addAttribute("settlementId", id);

            return "projects/settlement/edit";
        } catch (Exception e) {
            model.addAttribute("error", "정산 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/settlement/list";
        }
    }

    @PostMapping("/{id}/update")
    public String updateSettlement(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateSettlementRequest request,
            Model model) {
        try {
            SettlementResponse response = settlementService.updateSettlement(id, request);
            if (response.getSettlementStatus() != SettlementStatus.FINAL) {
                return "redirect:/settlements";
            }
            return "redirect:/settlements/" + id + "?updated";
        } catch (Exception e) {
            model.addAttribute("error", "정산 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/settlement/edit";
        }
    }

    @PostMapping("/{id}/payment-status")
    public String updatePaymentStatus(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdatePaymentStatusRequest request,
            Model model) {
        try {
            settlementService.updatePaymentStatus(id, request.getPaymentStatus());
            return "redirect:/settlements/" + id + "?statusUpdated";
        } catch (Exception e) {
            model.addAttribute("error", "결제 상태 변경 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/settlement/detail";
        }
    }

    @PatchMapping("/{id}/status")
    @ResponseBody
    public void updateStatus(
            @PathVariable Long id, @RequestBody UpdatePaymentStatusRequest request) {
        settlementService.updatePaymentStatus(id, request.getPaymentStatus());
    }
}
