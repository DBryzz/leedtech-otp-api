package org.leedtech.otp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.leedtech.otp.utils.commons.BaseEntity;

import java.math.BigDecimal;


/**
 *
 * @author DB.Tech
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enrollments")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class EnrollmentEntity extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal fees;

    @Column(nullable = false)
    private BigDecimal initialDeposit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity student;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    private AcademicLevelEntity academicLevel;

    @Column(unique = false, nullable = false)
    private String academicYear;
}
