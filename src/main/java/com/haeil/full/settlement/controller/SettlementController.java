package com.haeil.full.settlement.controller;

import com.haeil.full.settlement.dto.request.CreateSettlementRequest;
import com.haeil.full.settlement.dto.request.UpdatePaymentStatusRequest;
import com.haeil.full.settlement.dto.request.UpdateSettlementRequest;
import com.haeil.full.settlement.dto.response.SettlementListResponse;
import com.haeil.full.settlement.dto.response.SettlementResponse;
import com.haeil.full.settlement.service.SettlementService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/settlements")
@Controller
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/new")
    public String settlementForm(Model model) {
        model.addAttribute("request", new CreateSettlementRequest());
        return "settlement/form";
    }

    @PostMapping
    public String createSettlement(
            @Valid @ModelAttribute("request") CreateSettlementRequest request, Model model) {
        try {
            SettlementResponse response = settlementService.createSettlement(request);
            return "redirect:/settlements/" + response.getId() + "?created";
        } catch (Exception e) {
            model.addAttribute("error", "정산 생성 중 오류가 발생했습니다: " + e.getMessage());
            return "settlement/form";
        }
    }

    @GetMapping
    public String getSettlements(Model model) {
        List<SettlementListResponse> responses = settlementService.getSettlements();
        model.addAttribute("settlements", responses);
        return "settlement/list";
    }

    @GetMapping("/{id}")
    public String getSettlement(@PathVariable Long id, Model model) {
        try {
            SettlementResponse response = settlementService.getSettlement(id);
            model.addAttribute("settlement", response);
            model.addAttribute("updateRequest", new UpdateSettlementRequest());
            model.addAttribute("paymentStatusRequest", new UpdatePaymentStatusRequest(null));
            return "settlement/detail";
        } catch (Exception e) {
            model.addAttribute("error", "정산 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "settlement/list";
        }
    }

    @GetMapping("/{id}/edit")
    public String editSettlementForm(@PathVariable Long id, Model model) {
        try {
            SettlementResponse settlement = settlementService.getSettlement(id);
            model.addAttribute("settlement", settlement);
            model.addAttribute("updateRequest", new UpdateSettlementRequest());
            return "settlement/edit";
        } catch (Exception e) {
            model.addAttribute("error", "정산 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "settlement/list";
        }
    }

    @PostMapping("/{id}/update")
    public String updateSettlement(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateSettlementRequest request,
            Model model) {
        try {
            SettlementResponse response = settlementService.updateSettlement(id, request);
            return "redirect:/settlements/" + id + "?updated";
        } catch (Exception e) {
            model.addAttribute("error", "정산 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "settlement/edit";
        }
    }

    @PostMapping("/{id}/payment-status")
    public String updatePaymentStatus(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdatePaymentStatusRequest request,
            Model model) {
        try {
            SettlementResponse response =
                    settlementService.updatePaymentStatus(id, request.getPaymentStatus());
            return "redirect:/settlements/" + id + "?statusUpdated";
        } catch (Exception e) {
            model.addAttribute("error", "결제 상태 변경 중 오류가 발생했습니다: " + e.getMessage());
            return "settlement/detail";
        }
    }
}
