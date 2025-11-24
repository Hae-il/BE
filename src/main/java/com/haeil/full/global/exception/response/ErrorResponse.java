package com.haeil.full.global.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import org.springframework.validation.FieldError;

@Builder
public record ErrorResponse(
        Boolean isSuccess,
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) // 유효성 오류가 있을 때만 포함됨
                ValidationErrors results) {

    public record ValidationErrors(List<ValidationError> validationErrors) {}

    @Builder
    public record ValidationError(String field, String message) {
        public static ValidationError from(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
