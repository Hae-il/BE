package com.haeil.full.consultation.controller;

import com.haeil.full.consultation.domain.ConsultationFile;
import com.haeil.full.consultation.dto.request.ApproveConsultationReservation;
import com.haeil.full.consultation.dto.request.ConsultationNoteRequest;
import com.haeil.full.consultation.dto.request.CreateConsultationRequest;
import com.haeil.full.consultation.dto.request.CreateConsultationReservationRequest;
import com.haeil.full.consultation.dto.request.RejectConsultationReservation;
import com.haeil.full.consultation.dto.response.ConsultationNoteResponse;
import com.haeil.full.consultation.dto.response.ConsultationReservationResponse;
import com.haeil.full.consultation.dto.response.ConsultationResponse;
import com.haeil.full.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.BindingResult;

@RequestMapping("/consultations")
@Controller
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping("/reservations/new")
    public String reservationForm(Model model) {
        model.addAttribute("request", new CreateConsultationReservationRequest());
        return "projects/consultations/reservation_form";
    }

    @PostMapping("/reservations")
    public String createConsultationReservation(
            @Valid @ModelAttribute("request") CreateConsultationReservationRequest request,
            BindingResult bindingResult,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            return "projects/consultations/reservation_form";
        }

        try {
            ConsultationReservationResponse response =
                    consultationService.createConsultationReservation(request);
            model.addAttribute("response", response);
            return "redirect:/consultations/reservations?success";
        } catch (Exception e) {
            model.addAttribute("error", "상담 예약 신청 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/reservation_form";
        }
    }

    @GetMapping("/reservations")
    public String getConsultationReservations(Model model) {
        List<ConsultationReservationResponse> responses =
                consultationService.getConsultationReservations();
        model.addAttribute("reservations", responses);
        return "projects/consultations/reservation_list";
    }

    @GetMapping("/reservations/{id}")
    public String getConsultationRequest(@PathVariable Long id, Model model) {
        try {
            ConsultationReservationResponse response =
                    consultationService.getConsultationRequest(id);
            model.addAttribute("reservation", response);
            model.addAttribute("approveRequest", new ApproveConsultationReservation());
            model.addAttribute("rejectRequest", new RejectConsultationReservation());
            return "projects/consultations/reservation_details";
        } catch (Exception e) {
            model.addAttribute("error", "상담 예약 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/reservation_list";
        }
    }

    @PostMapping("/reservations/{id}/approve")
    public String approveConsultationReservation(
            @PathVariable Long id,
            @Valid @ModelAttribute("approveRequest") ApproveConsultationReservation request,
            BindingResult bindingResult,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            // 에러 발생 시 다시 상세 페이지로 이동하며 데이터를 채워줘야 함
            ConsultationReservationResponse response = consultationService.getConsultationRequest(id);
            model.addAttribute("reservation", response);
            model.addAttribute("rejectRequest", new RejectConsultationReservation());
            return "projects/consultations/reservation_details";
        }

        try {
            ConsultationReservationResponse response =
                    consultationService.approveConsultationReservation(id, request);
            return "redirect:/consultations/reservations/" + id + "?approved";
        } catch (Exception e) {
            model.addAttribute("error", "상담 예약 승인 중 오류가 발생했습니다: " + e.getMessage());
            // 에러 발생 시에도 상세 페이지 데이터 필요
            ConsultationReservationResponse response = consultationService.getConsultationRequest(id);
            model.addAttribute("reservation", response);
            model.addAttribute("rejectRequest", new RejectConsultationReservation());
            return "projects/consultations/reservation_details";
        }
    }

    @PostMapping("/reservations/{id}/reject")
    public String rejectConsultationReservation(
            @PathVariable Long id,
            @Valid @ModelAttribute("rejectRequest") RejectConsultationReservation request,
            BindingResult bindingResult,
            Model model) {
            
        if (bindingResult.hasErrors()) {
            ConsultationReservationResponse response = consultationService.getConsultationRequest(id);
            model.addAttribute("reservation", response);
            model.addAttribute("approveRequest", new ApproveConsultationReservation());
            return "projects/consultations/reservation_details";
        }

        try {
            ConsultationReservationResponse response =
                    consultationService.rejectConsultationReservation(id, request);
            return "redirect:/consultations/reservations/" + id + "?rejected";
        } catch (Exception e) {
            model.addAttribute("error", "상담 예약 거절 중 오류가 발생했습니다: " + e.getMessage());
            ConsultationReservationResponse response = consultationService.getConsultationRequest(id);
            model.addAttribute("reservation", response);
            model.addAttribute("approveRequest", new ApproveConsultationReservation());
            return "projects/consultations/reservation_details";
        }
    }

    @GetMapping("/new")
    public String consultationForm(
            @RequestParam(value = "reservationId", required = false) Long reservationId,
            Model model) {
        
        CreateConsultationRequest request = new CreateConsultationRequest();
        
        if (reservationId != null) {
            try {
                ConsultationReservationResponse reservation = consultationService.getConsultationRequest(reservationId);
                request.setReservationId(reservationId);
                request.setCounselorId(reservation.getAssignedLawyerId());
                request.setConsultationDate(reservation.getRequestedDate());
                request.getClient().setName(reservation.getName());
                request.getClient().setPhone(reservation.getPhone());
            } catch (Exception e) {
                // 예약 정보를 찾을 수 없는 경우 무시하고 빈 폼 출력
            }
        }
        
        model.addAttribute("request", request);
        return "projects/consultations/form";
    }

    @PostMapping
    public String createConsultation(
            @Valid @ModelAttribute("request") CreateConsultationRequest request,
            BindingResult bindingResult,
            Model model) {
            
        if (bindingResult.hasErrors()) {
            return "projects/consultations/form";
        }

        try {
            ConsultationResponse response = consultationService.createConsultation(request);
            return "redirect:/consultations/" + response.getId() + "?created";
        } catch (Exception e) {
            model.addAttribute("error", "상담 시작 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/form";
        }
    }

    @GetMapping
    public String getConsultations(Model model) {
        List<ConsultationResponse> responses = consultationService.getConsultations();
        model.addAttribute("consultations", responses);
        return "projects/consultations/list";
    }

    @GetMapping("/{id}")
    public String getConsultation(@PathVariable Long id, Model model) {
        try {
            ConsultationResponse response = consultationService.getConsultation(id);
            List<ConsultationNoteResponse> notes = consultationService.getConsultationNotes(id);
            List<ConsultationFile> files = consultationService.getConsultationFiles(id);

            model.addAttribute("consultation", response);
            model.addAttribute("notes", notes);
            model.addAttribute("files", files);
            model.addAttribute("consultationFiles", files);
            if (!notes.isEmpty()) {
                model.addAttribute("consultationNote", notes.get(0));
            }
            
            model.addAttribute("tasks", java.util.Collections.emptyList());
            model.addAttribute("activities", java.util.Collections.emptyList());
            model.addAttribute("teamMembers", java.util.Collections.emptyList());
            
            model.addAttribute("noteRequest", new ConsultationNoteRequest());
            return "projects/consultations/details";
        } catch (Exception e) {
            model.addAttribute("error", "상담 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/list";
        }
    }

    @PostMapping("/{id}/start")
    public String startConsultation(@PathVariable Long id, Model model) {
        try {
            ConsultationResponse response = consultationService.startConsultation(id);
            return "redirect:/consultations/" + id + "?started";
        } catch (Exception e) {
            model.addAttribute("error", "상담 시작 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/details";
        }
    }

    @PostMapping("/{id}/complete")
    public String completeConsultation(@PathVariable Long id, Model model) {
        try {
            ConsultationResponse response = consultationService.completeConsultation(id);
            return "redirect:/consultations/" + id + "?completed";
        } catch (Exception e) {
            model.addAttribute("error", "상담 완료 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/details";
        }
    }

    @PostMapping("/{id}/notes")
    public String createConsultationNote(
            @PathVariable Long id,
            @Valid @ModelAttribute ConsultationNoteRequest request,
            BindingResult bindingResult,
            Model model) {
            
        if (bindingResult.hasErrors()) {
            // 에러 발생 시 상세 페이지 데이터 재조회 필요
            return getConsultation(id, model);
        }

        try {
            ConsultationNoteResponse response =
                    consultationService.createConsultationNote(id, request);
            return "redirect:/consultations/" + id + "?noteCreated";
        } catch (Exception e) {
            model.addAttribute("error", "노트 작성 중 오류가 발생했습니다: " + e.getMessage());
            // 에러 발생 시 상세 페이지 데이터 재조회 필요
            return getConsultation(id, model);
        }
    }

    @GetMapping("/{id}/notes")
    public String getConsultationNotes(@PathVariable Long id, Model model) {
        try {
            List<ConsultationNoteResponse> responses = consultationService.getConsultationNotes(id);
            model.addAttribute("notes", responses);
            model.addAttribute("consultationId", id);
            return "projects/consultations/details"; 
        } catch (Exception e) {
            model.addAttribute("error", "노트 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/details";
        }
    }

    @PostMapping("/{consultationId}/notes/{noteId}")
    public String updateConsultationNote(
            @PathVariable Long consultationId,
            @PathVariable Long noteId,
            @Valid @ModelAttribute ConsultationNoteRequest request,
            BindingResult bindingResult,
            Model model) {
            
        if (bindingResult.hasErrors()) {
             return getConsultation(consultationId, model);
        }

        try {
            ConsultationNoteResponse response =
                    consultationService.updateConsultationNote(consultationId, noteId, request);
            return "redirect:/consultations/" + consultationId + "/notes?updated";
        } catch (Exception e) {
            model.addAttribute("error", "노트 수정 중 오류가 발생했습니다: " + e.getMessage());
            return getConsultation(consultationId, model);
        }
    }

    @PostMapping("/{id}/files")
    public String uploadConsultationFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            Model model)
            throws IOException {
        try {
            String response = consultationService.uploadConsultationFile(id, file, description);
            return "redirect:/consultations/" + id + "?fileUploaded";
        } catch (Exception e) {
            model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/details";
        }
    }

    @GetMapping("/{id}/files")
    public String getConsultationFiles(@PathVariable Long id, Model model) {
        try {
            List<ConsultationFile> files = consultationService.getConsultationFiles(id);
            model.addAttribute("files", files);
            model.addAttribute("consultationId", id);
            return "projects/consultations/details";
        } catch (Exception e) {
            model.addAttribute("error", "파일 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/details";
        }
    }

    @PostMapping("/{consultationId}/counselor")
    public String updateCounselor(
            @PathVariable Long consultationId, @RequestParam Long newCounselorId, Model model) {
        try {
            ConsultationResponse response =
                    consultationService.updateCounselor(consultationId, newCounselorId);
            return "redirect:/consultations/" + consultationId + "?counselorUpdated";
        } catch (Exception e) {
            model.addAttribute("error", "담당자 변경 중 오류가 발생했습니다: " + e.getMessage());
            return "projects/consultations/details";
        }
    }
}
