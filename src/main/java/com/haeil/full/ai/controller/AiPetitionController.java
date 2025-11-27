package com.haeil.full.ai.controller;

import com.haeil.full.ai.dto.request.AiPetitionRequest;
import com.haeil.full.ai.service.AiPetitionService;
import com.haeil.full.cases.dto.response.PetitionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiPetitionController {

    private final AiPetitionService aiPetitionService;

    @PostMapping("/petition/draft")
    public String generatePetitionDraft(
            @Valid @ModelAttribute AiPetitionRequest request, Model model) {
        PetitionResponse response = aiPetitionService.generatePetitionDraft(request.getCaseId());
        model.addAttribute("petition", response);
        return "case/petition";
    }
}
