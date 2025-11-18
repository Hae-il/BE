package com.haeil.be.settlement.domain;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.settlement.domain.type.PaymentStatus;
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

    @Column(name = "lawyer_fee", nullable = false, precision = 19, scale = 2)
    private BigDecimal lawyerFee;

    @Column(name = "total", nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @Column(name = "expenses", nullable = false, precision = 19, scale = 2)
    private BigDecimal expenses;

    @Column(name = "is_vat_included", nullable = false)
    private Boolean isVatIncluded = false;

    @Column(name = "client_receivable", nullable = false, precision = 19, scale = 2)
    private BigDecimal clientReceivable;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Cases relatedCase;

    @Builder
    public Settlement(
            PaymentStatus paymentStatus,
            BigDecimal lawyerFee,
            BigDecimal total,
            BigDecimal expenses,
            Boolean isVatIncluded,
            BigDecimal clientReceivable,
            LocalDate settlementDate,
            LocalDate dueDate,
            String note,
            Cases relatedCase) {
        this.paymentStatus = paymentStatus;
        this.lawyerFee = lawyerFee;
        this.total = total;
        this.expenses = expenses;
        this.isVatIncluded = isVatIncluded != null ? isVatIncluded : false;
        this.clientReceivable = clientReceivable;
        this.settlementDate = settlementDate;
        this.dueDate = dueDate;
        this.note = note;
        this.relatedCase = relatedCase;
    }

    /**
     * 정산 정보를 업데이트합니다.
     *
     * @param paymentStatus 결제 상태
     * @param lawyerFee 변호사 수임료
     * @param total 총액
     * @param expenses 경비
     * @param isVatIncluded VAT 포함 여부
     * @param clientReceivable 의뢰인 미수금
     * @param settlementDate 정산일
     * @param dueDate 만료일
     * @param note 비고
     */
    public void update(
            PaymentStatus paymentStatus,
            BigDecimal lawyerFee,
            BigDecimal total,
            BigDecimal expenses,
            Boolean isVatIncluded,
            BigDecimal clientReceivable,
            LocalDate settlementDate,
            LocalDate dueDate,
            String note) {
        if (paymentStatus != null) {
            this.changePaymentStatus(paymentStatus);
        }
        if (lawyerFee != null) {
            this.lawyerFee = lawyerFee;
        }
        if (total != null) {
            this.total = total;
        }
        if (expenses != null) {
            this.expenses = expenses;
        }
        if (isVatIncluded != null) {
            this.isVatIncluded = isVatIncluded;
        }
        if (clientReceivable != null) {
            this.clientReceivable = clientReceivable;
        }
        if (settlementDate != null) {
            this.settlementDate = settlementDate;
        }
        if (dueDate != null) {
            this.dueDate = dueDate;
        }
        if (note != null) {
            this.note = note;
        }

        // 금액이 변경된 경우 clientReceivable 재계산
        if (total != null || lawyerFee != null || expenses != null || isVatIncluded != null) {
            this.recalculateClientReceivable();
        }
    }

    /**
     * clientReceivable을 계산합니다. 공식: clientReceivable = total - (lawyerFee + expenses + VAT) VAT =
     * (lawyerFee + expenses) * 0.1
     *
     * @param total 합의금 전체 금액
     * @param lawyerFee 변호사 보수
     * @param expenses 실비
     * @param isVatIncluded VAT 포함 여부
     * @return 계산된 의뢰인 최종 수령액
     */
    public static BigDecimal calculateClientReceivable(
            BigDecimal total, BigDecimal lawyerFee, BigDecimal expenses, Boolean isVatIncluded) {
        // 변호사 보수 + 실비
        BigDecimal subtotal = lawyerFee.add(expenses);

        // VAT 계산: (lawyerFee + expenses) * 0.1
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

        // clientReceivable = total - deduction
        return total.subtract(deduction);
    }

    /** clientReceivable을 재계산하여 업데이트합니다. */
    public void recalculateClientReceivable() {
        this.clientReceivable =
                calculateClientReceivable(
                        this.total, this.lawyerFee, this.expenses, this.isVatIncluded);
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
