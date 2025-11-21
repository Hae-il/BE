package com.haeil.full.client.repository;

import com.haeil.full.client.domain.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByResidentNumber(String residentNumber);

    boolean existsByResidentNumber(String residentNumber);
}
