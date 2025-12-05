package com.haeil.full.ai.controller;

import com.haeil.full.ai.service.AiReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiReviewController {

    private final AiReviewService aiReviewService;

    @PostMapping("/review/{caseId}")
    public String reviewPetitionByCaseId(@PathVariable Long caseId, Model model) {
        String result = aiReviewService.reviewPetition(caseId);
        model.addAttribute("reviewResult", result);
        return "projects/cases/review-result";
    }
}
