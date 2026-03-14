package org.leedtech.otp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.leedtech.otp.utils.commons.BaseEntity;

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
    Double paymentAmount;

//    Incentive rate,
    float incentiveRate;

//    Incentive amount,
    Double incentiveAmount;

//    Total payment amount,
    Double totalPaymentAmount;

//    Previous balance,
//    New balance,
    Double newBalance;

    LocalDate paymentDate;

    //    Next payment due date,
    LocalDate nextPaymentDueDate;

    //    Student Number
//    String studentNumber,
    @Column(nullable = true, columnDefinition = "TEXT")
    private String studentNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private EnrollmentEntity enrollment;

    private boolean isComplete() {
        return enrollment.getFees().equals(newBalance);
    }
}
