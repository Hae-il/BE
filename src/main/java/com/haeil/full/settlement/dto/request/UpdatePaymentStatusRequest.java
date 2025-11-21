package com.haeil.full.settlement.dto.request;

import com.haeil.full.settlement.domain.type.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePaymentStatusRequest {

    @NotNull(message = "결제 상태는 필수 입력 항목입니다.")
    private PaymentStatus paymentStatus;

    @Builder
    public UpdatePaymentStatusRequest(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
