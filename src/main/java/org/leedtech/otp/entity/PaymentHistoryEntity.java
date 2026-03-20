package org.leedtech.otp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.leedtech.otp.utils.commons.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 *
 * @author DB.Tech
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_histories")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PaymentHistoryEntity extends BaseEntity {

//    Payment amount,
    BigDecimal paymentAmount;

//    Incentive rate,
    float incentiveRate;

//    Incentive amount,
    BigDecimal incentiveAmount;

//    Total payment amount,
    BigDecimal totalPaymentAmount;

//    Previous balance,
    BigDecimal previousAmount;

    //    New balance,
    BigDecimal newBalance;

    LocalDate paymentDate;

    //    Next payment due date,
    LocalDate nextPaymentDueDate;

    //    Student Number
//    String studentNumber,
    @Column(nullable = true, columnDefinition = "TEXT")
    private String studentNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private EnrollmentEntity enrollment;

    public boolean isInGoodStanding() {
        return this.getNewBalance().compareTo(BigDecimal.valueOf(0)) <= 0;
    }
}
