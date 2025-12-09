package com.haeil.full.settlement.controller;

import com.haeil.full.cases.dto.response.CaseInfoResponse;
import com.haeil.full.cases.service.CasesService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/settlements")
@Controller
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;
    private final CasesService casesService;

    @GetMapping("/new")
    public String settlementForm(
            @RequestParam(required = false) Long caseId,
            @RequestParam(required = false) String caseNumber,
            Model model) {
        CreateSettlementRequest request = new CreateSettlementRequest();
        if (caseId != null) {
            request.setCaseId(caseId);

            try {
                CaseInfoResponse caseInfo = casesService.getCaseInfo(caseId);
                model.addAttribute("clientName", caseInfo.clientName());
                model.addAttribute("attorneyName", caseInfo.attorneyName());
                model.addAttribute("caseType", caseInfo.caseType());
            } catch (Exception e) {
                // Case not found or other error
                model.addAttribute("clientName", "-");
                model.addAttribute("attorneyName", "-");
                model.addAttribute("caseType", "-");
            }
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
    public String getSettlement(
            @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            SettlementResponse response = settlementService.getSettlement(id);
            model.addAttribute("settlement", response);

            if (response.getCaseId() != null) {
                CaseInfoResponse caseInfo = casesService.getCaseInfo(response.getCaseId());

                model.addAttribute("clientName", caseInfo.clientName());
                model.addAttribute("attorneyName", caseInfo.attorneyName());
                model.addAttribute("caseType", caseInfo.caseType());
            } else {
                model.addAttribute("clientName", "-");
                model.addAttribute("attorneyName", "-");
                model.addAttribute("caseType", "-");
            }

            model.addAttribute("updateRequest", new UpdateSettlementRequest());
            model.addAttribute("paymentStatusRequest", new UpdatePaymentStatusRequest(null));
            return "projects/settlement/detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "정산 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/settlements";
        }
    }

    @GetMapping("/{id}/edit")
    public String editSettlementForm(
            @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
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
                CaseInfoResponse caseInfo = casesService.getCaseInfo(caseId);

                model.addAttribute("caseNumber", "C" + caseId);
                model.addAttribute("clientName", caseInfo.clientName());
                model.addAttribute("attorneyName", caseInfo.attorneyName());
                model.addAttribute("caseType", caseInfo.caseType());
            }

            model.addAttribute("request", request);
            model.addAttribute("settlementId", id);

            return "projects/settlement/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "정산 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/settlements";
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
            return "redirect:/settlements/" + id;
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
