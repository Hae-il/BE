package com.haeil.be.client.dto.response;

import com.haeil.be.client.domain.Client;
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

    public static ClientResponse from(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .residentNumber(client.getResidentNumber())
                .birthDate(client.getBirthDate())
                .gender(client.getGender() != null ? client.getGender().name() : null)
                .jobTitle(client.getJobTitle())
                .build();
    }
}
