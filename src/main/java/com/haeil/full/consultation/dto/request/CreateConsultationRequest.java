package com.haeil.full.consultation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haeil.full.client.dto.request.CreateClientRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class CreateConsultationRequest {

    @NotNull(message = "상담 예약 ID는 필수 입력 항목입니다.")
    private Long reservationId;

    @Valid
    @NotNull(message = "의뢰인 정보는 필수 입력 항목입니다.")
    private CreateClientRequest client = new CreateClientRequest();

    @NotNull(message = "담당 상담사 ID는 필수 입력 항목입니다.")
    private Long counselorId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime consultationDate;
}
