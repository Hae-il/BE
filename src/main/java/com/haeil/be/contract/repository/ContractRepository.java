package com.haeil.be.contract.repository;

import com.haeil.be.contract.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {}
