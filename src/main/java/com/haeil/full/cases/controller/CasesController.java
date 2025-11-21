package com.haeil.full.cases.controller;

import com.haeil.full.cases.dto.request.AssignAttorneyRequest;
import com.haeil.full.cases.dto.request.CaseDocumentRequest;
import com.haeil.full.cases.dto.request.CaseNumberRequest;
import com.haeil.full.cases.dto.request.DecisionRequest;
import com.haeil.full.cases.dto.request.PetitionRequest;
import com.haeil.full.cases.service.CasesService;
import com.haeil.full.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CasesController {

    private final CasesService casesService;

    @GetMapping("/unassigned")
    public String getUnassignedCases(Model model) {
        model.addAttribute("cases", casesService.getUnassignedCases());
        return "case/unassigned";
    }

    @GetMapping("/unassigned/{caseId}")
    public String getUnassignedCaseDetail(@PathVariable Long caseId, Model model) {
        model.addAttribute("case", casesService.getUnassignedCaseDetail(caseId));
        return "case/unassigned-detail";
    }

    @PostMapping("/unassigned/{caseId}/assign")
    public String assignAttorney(
            @PathVariable Long caseId, @ModelAttribute AssignAttorneyRequest request) {
        casesService.assignAttorney(caseId, request);
        return "redirect:/cases/unassigned";
    }

    @GetMapping("/requested")
    public String getRequestedCases(
            @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("cases", casesService.getRequestedCases(userDetails.getId()));
        return "case/requested";
    }

    @GetMapping("/requested/{caseId}")
    public String getRequestedCaseDetail(
            @PathVariable Long caseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        model.addAttribute(
                "case", casesService.getRequestedCaseDetail(caseId, userDetails.getId()));
        return "case/requested-detail";
    }

    @PostMapping("/requested/{caseId}")
    public String decideCaseAssignment(
            @PathVariable Long caseId,
            @ModelAttribute DecisionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.decideCaseAssignment(caseId, request, userDetails.getId());
        return "redirect:/cases/requested";
    }

    @GetMapping("/ongoing")
    public String getOngoingCases(
            @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("cases", casesService.getOngoingCases(userDetails.getId()));
        return "case/ongoing";
    }

    @GetMapping("/ongoing/{caseId}")
    public String getOngoingCaseDetail(
            @PathVariable Long caseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        model.addAttribute("case", casesService.getOngoingCaseDetail(caseId, userDetails.getId()));
        return "case/ongoing-detail";
    }

    @PostMapping("/ongoing/{caseId}/petition")
    public String createPetition(
            @PathVariable Long caseId,
            @ModelAttribute PetitionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.createPetition(caseId, request, userDetails.getId());
        return "redirect:/cases/ongoing/" + caseId;
    }

    @GetMapping("/ongoing/{caseId}/petition")
    public String getPetition(
            @PathVariable Long caseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        model.addAttribute("petition", casesService.getPetition(caseId, userDetails.getId()));
        return "case/petition";
    }

    @PostMapping("/ongoing/{caseId}/petition/update")
    public String updatePetition(
            @PathVariable Long caseId,
            @ModelAttribute PetitionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.updatePetition(caseId, request, userDetails.getId());
        return "redirect:/cases/ongoing/" + caseId;
    }

    @PostMapping("/ongoing/{caseId}/caseNumber")
    public String updateCaseNumber(
            @PathVariable Long caseId,
            @ModelAttribute CaseNumberRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.updateCaseNumber(caseId, request.caseNumber(), userDetails.getId());
        return "redirect:/cases/ongoing/" + caseId;
    }

    @PostMapping("/ongoing/{caseId}/documents")
    public String uploadCaseDocument(
            @PathVariable Long caseId,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute CaseDocumentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails)
            throws java.io.IOException {
        casesService.uploadCaseDocument(caseId, file, request, userDetails.getId());
        return "redirect:/cases/ongoing/" + caseId + "/documents";
    }

    @GetMapping("/ongoing/{caseId}/documents")
    public String getCaseDocuments(
            @PathVariable Long caseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        model.addAttribute("documents", casesService.getCaseDocuments(caseId, userDetails.getId()));
        model.addAttribute("caseId", caseId);
        return "case/documents";
    }

    @PostMapping("/ongoing/{caseId}/documents/{documentId}/delete")
    public String deleteCaseDocument(
            @PathVariable Long caseId,
            @PathVariable Long documentId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
            throws java.io.IOException {
        casesService.deleteCaseDocument(caseId, documentId, userDetails.getId());
        return "redirect:/cases/ongoing/" + caseId + "/documents";
    }

    @PostMapping("/ongoing/{caseId}/complete")
    public String completeCase(
            @PathVariable Long caseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.completeCase(caseId, userDetails.getId());
        return "redirect:/cases/completed";
    }

    @GetMapping("/completed")
    public String getCompletedCases(
            @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("cases", casesService.getCompletedCases(userDetails.getId()));
        return "case/completed";
    }

    @GetMapping("/completed/{caseId}")
    public String getCompletedCaseDetail(
            @PathVariable Long caseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        model.addAttribute(
                "case", casesService.getCompletedCaseDetail(caseId, userDetails.getId()));
        return "case/completed-detail";
    }
}
