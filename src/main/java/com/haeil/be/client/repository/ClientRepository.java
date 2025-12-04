package com.haeil.be.client.repository;

import com.haeil.be.client.domain.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByResidentNumber(String residentNumber);

    Optional<Client> findByEmail(String email);
}
