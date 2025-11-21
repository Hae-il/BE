package com.haeil.full.client.dto.request;

import com.haeil.full.client.domain.type.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CreateClientRequest {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^01[0-9]-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    private String address;
    private String residentNumber;
    private LocalDate birthDate;

    private Gender gender;

    private String jobTitle;

    @Builder
    public CreateClientRequest(
            String name,
            String email,
            String phone,
            String address,
            String residentNumber,
            LocalDate birthDate,
            Gender gender,
            String jobTitle) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.residentNumber = residentNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.jobTitle = jobTitle;
    }
}
