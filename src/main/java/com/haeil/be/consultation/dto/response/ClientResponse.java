package com.haeil.be.consultation.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String residentNumber;
    private LocalDate birthDate;
    private String gender;
    private String jobTitle;
}
