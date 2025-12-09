package com.haeil.full.client.service;

import com.haeil.full.client.domain.Client;
import com.haeil.full.client.dto.request.CreateClientRequest;
import com.haeil.full.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client getClient(CreateClientRequest request) {
        if (request.getResidentNumber() != null) {
            return clientRepository
                    .findByResidentNumber(request.getResidentNumber())
                    .map(
                            client -> {
                                client.update(
                                        request.getName(),
                                        request.getEmail(),
                                        request.getPhone(),
                                        request.getAddress(),
                                        request.getBirthDate(),
                                        request.getGender(),
                                        request.getJobTitle());
                                return client;
                            })
                    .orElseGet(() -> createNewClient(request));
        }
        return createNewClient(request);
    }

    @Transactional
    public Client createNewClient(CreateClientRequest request) {
        Client client =
                Client.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .address(request.getAddress())
                        .residentNumber(request.getResidentNumber())
                        .birthDate(request.getBirthDate())
                        .gender(request.getGender())
                        .jobTitle(request.getJobTitle())
                        .build();

        return clientRepository.save(client);
    }
}
