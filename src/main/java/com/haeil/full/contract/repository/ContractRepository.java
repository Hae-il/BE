package com.haeil.full.contract.repository;

import com.haeil.full.contract.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {}
