package com.haeil.be.contract.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("정액")
@Getter
@NoArgsConstructor
public class FixedFeeContract extends Contract {

    @OneToMany(mappedBy = "fixedFeeContract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractCondition> contractConditionList = new ArrayList<>();

}
