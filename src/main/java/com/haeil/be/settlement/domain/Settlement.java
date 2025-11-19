package com.haeil.be.settlement.domain;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.settlement.domain.type.PaymentStatus;
import com.haeil.be.settlement.domain.type.SettlementStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "settlements")
@Entity
@Getter
@NoArgsConstructor
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_status", nullable = false)
    private SettlementStatus settlementStatus = SettlementStatus.NONE;

    @Column(name = "attorney_fee", precision = 19, scale = 2)
    private BigDecimal attorneyFee;

    @Column(name = "agreement_amount", precision = 19, scale = 2)
    private BigDecimal agreementAmount;

    @Column(name = "expenses", precision = 19, scale = 2)
    private BigDecimal expenses;

    @Column(name = "is_vat_included", nullable = false)
    private Boolean isVatIncluded = false;

    @Column(name = "client_receivable", precision = 19, scale = 2)
    private BigDecimal clientReceivable;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", unique = true)
    private Cases cases;

    @Builder
    public Settlement(
            PaymentStatus paymentStatus,
            SettlementStatus settlementStatus,
            BigDecimal attorneyFee,
            BigDecimal agreementAmount,
            BigDecimal expenses,
            Boolean isVatIncluded,
            BigDecimal clientReceivable,
            LocalDate settlementDate,
            LocalDate paymentDueDate,
            String note,
            Cases cases) {
        this.paymentStatus = paymentStatus;
        this.settlementStatus = settlementStatus != null ? settlementStatus : SettlementStatus.NONE;
        this.attorneyFee = attorneyFee;
        this.agreementAmount = agreementAmount;
        this.expenses = expenses;
        this.isVatIncluded = isVatIncluded != null ? isVatIncluded : false;
        this.clientReceivable = clientReceivable;
        this.settlementDate = settlementDate;
        this.paymentDueDate = paymentDueDate;
        this.note = note;
        this.cases = cases;
    }

    /**
     * 정산 정보를 업데이트합니다.
     *
     * @param attorneyFee 변호사 보수
     * @param agreementAmount 합의금 총액
     * @param expenses 경비
     * @param isVatIncluded VAT 포함 여부
     * @param settlementDate 합의일자
     * @param paymentDueDate 입금기한
     * @param note 비고
     */
    public void update(
            BigDecimal attorneyFee,
            BigDecimal agreementAmount,
            BigDecimal expenses,
            Boolean isVatIncluded,
            LocalDate settlementDate,
            LocalDate paymentDueDate,
            String note) {
        if (attorneyFee != null) {
            this.attorneyFee = attorneyFee;
        }
        if (agreementAmount != null) {
            this.agreementAmount = agreementAmount;
        }
        if (expenses != null) {
            this.expenses = expenses;
        }
        if (isVatIncluded != null) {
            this.isVatIncluded = isVatIncluded;
        }
        if (settlementDate != null) {
            this.settlementDate = settlementDate;
        }
        if (paymentDueDate != null) {
            this.paymentDueDate = paymentDueDate;
        }
        if (note != null) {
            this.note = note;
        }

        // 상태 변경 규칙 적용
        updateSettlementStatus();

        // 금액이 변경된 경우 clientReceivable 자동 재계산
        if (agreementAmount != null
                || attorneyFee != null
                || expenses != null
                || isVatIncluded != null) {
            this.recalculateClientReceivable();
        }
    }

    /**
     * 정산서 상태를 자동으로 업데이트합니다. - NONE → FINAL: 필수 필드 모두 채워지면 - NONE → DRAFT: 어떤 필드든 값이 입력되면 (필수 필드가 모두
     * 채워지지 않은 경우) - DRAFT → FINAL: 필수 필드 모두 채워지면
     */
    public void updateSettlementStatus() {
        if (this.settlementStatus == SettlementStatus.NONE) {
            if (isAllRequiredFieldsFilled()) {
                // 필수 필드 모두 채워지면 → FINAL로 직접 변경
                this.settlementStatus = SettlementStatus.FINAL;
            } else if (hasAnyFieldValue()) {
                // 필수 필드가 모두 채워지지 않았지만 어떤 필드든 값이 입력되면 → DRAFT로 변경
                this.settlementStatus = SettlementStatus.DRAFT;
            }
            return;
        }

        if (this.settlementStatus == SettlementStatus.DRAFT) {
            if (isAllRequiredFieldsFilled()) {
                // 필수 필드 모두 채워지면 → FINAL로 변경
                this.settlementStatus = SettlementStatus.FINAL;
            }
        }
    }

    /** 어떤 필드든 값이 입력되었는지 확인합니다. */
    private boolean hasAnyFieldValue() {
        return attorneyFee != null
                || agreementAmount != null
                || expenses != null
                || settlementDate != null
                || paymentDueDate != null;
        // || (note != null && !note.trim().isEmpty());
    }

    /** FINAL 상태로 변경하기 위한 필수 필드가 모두 채워졌는지 확인합니다. */
    private boolean isAllRequiredFieldsFilled() {
        return attorneyFee != null
                && agreementAmount != null
                && expenses != null
                && paymentDueDate != null;
    }

    /**
     * clientReceivable을 계산합니다. 공식: clientReceivable = agreementAmount - (attorneyFee + expenses +
     * VAT) VAT = (attorneyFee + expenses) * 0.1
     *
     * @param agreementAmount 합의금 전체 금액
     * @param attorneyFee 변호사 보수
     * @param expenses 실비
     * @param isVatIncluded VAT 포함 여부
     * @return 계산된 의뢰인 최종 수령액
     */
    public static BigDecimal calculateClientReceivable(
            BigDecimal agreementAmount,
            BigDecimal attorneyFee,
            BigDecimal expenses,
            Boolean isVatIncluded) {
        // 필수 필드가 null이면 null 반환
        if (agreementAmount == null || attorneyFee == null || expenses == null) {
            return null;
        }

        // 변호사 보수 + 실비
        BigDecimal subtotal = attorneyFee.add(expenses);

        // VAT 계산: (attorneyFee + expenses) * 0.1
        BigDecimal vat = subtotal.multiply(new BigDecimal("0.1"));

        // VAT 포함 여부에 따라 차감 금액 결정
        BigDecimal deduction;
        if (Boolean.TRUE.equals(isVatIncluded)) {
            // VAT 포함: subtotal + VAT를 차감
            deduction = subtotal.add(vat);
        } else {
            // VAT 미포함: subtotal만 차감
            deduction = subtotal;
        }

        // clientReceivable = agreementAmount - deduction
        return agreementAmount.subtract(deduction);
    }

    /** clientReceivable을 재계산하여 업데이트합니다. */
    public void recalculateClientReceivable() {
        this.clientReceivable =
                calculateClientReceivable(
                        this.agreementAmount, this.attorneyFee, this.expenses, this.isVatIncluded);
    }

    /**
     * paymentStatus를 변경합니다.
     *
     * @param newStatus 변경할 결제 상태
     */
    public void changePaymentStatus(PaymentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("결제 상태는 null일 수 없습니다.");
        }
        this.paymentStatus = newStatus;
    }
}
