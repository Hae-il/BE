package com.haeil.be.cases.domain;

import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "petition")
@Entity
@Getter
@NoArgsConstructor
public class Petition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", unique = true)
    private Cases cases;

    // 청구 취지
    @Column(name = "claim_amount")  //청구금액
    private Long claimAmount;

    @Column(name = "claim_content", columnDefinition = "TEXT") //청구내용
    private String claimContent;

    // 청구 원인
    @Column(name = "accident_circumstances", columnDefinition = "TEXT")  //사고경위
    private String accidentCircumstances;

    @Column(name = "damage_items", columnDefinition = "TEXT")  //손해항목
    private String damageItems;

    @Column(name = "damage_calculation", columnDefinition = "TEXT")  //손해액상정
    private String damageCalculation;

    @Column(name = "liability_basis", columnDefinition = "TEXT")  //책임 인정 근거
    private String liabilityBasis;

    // 입증 방법
    @Column(name = "proof_method", columnDefinition = "TEXT")
    private String proofMethod;

    // 첨부 서류
    @Column(name = "attached_documents", columnDefinition = "TEXT")
    private String attachedDocuments;

    @Builder
    public Petition(
            Cases cases,
            //청구취지
            Long claimAmount,
            String claimContent,
            //청구원인
            String accidentCircumstances,
            String damageItems,
            String damageCalculation,
            String liabilityBasis,
            //입증방법
            String proofMethod,
            //첨부 서류(당장은 string으로 처리했습니다)
            String attachedDocuments) {
        this.cases = cases;
        this.claimAmount = claimAmount;
        this.claimContent = claimContent;
        this.accidentCircumstances = accidentCircumstances;
        this.damageItems = damageItems;
        this.damageCalculation = damageCalculation;
        this.liabilityBasis = liabilityBasis;
        this.proofMethod = proofMethod;
        this.attachedDocuments = attachedDocuments;
    }

    public void updateClaimAmount(Long claimAmount) {
        this.claimAmount = claimAmount;
    }

    public void updateClaimContent(String claimContent) {
        this.claimContent = claimContent;
    }

    public void updateAccidentCircumstances(String accidentCircumstances) {
        this.accidentCircumstances = accidentCircumstances;
    }

    public void updateDamageItems(String damageItems) {
        this.damageItems = damageItems;
    }

    public void updateDamageCalculation(String damageCalculation) {
        this.damageCalculation = damageCalculation;
    }

    public void updateLiabilityBasis(String liabilityBasis) {
        this.liabilityBasis = liabilityBasis;
    }

    public void updateProofMethod(String proofMethod) {
        this.proofMethod = proofMethod;
    }

    public void updateAttachedDocuments(String attachedDocuments) {
        this.attachedDocuments = attachedDocuments;
    }
}

