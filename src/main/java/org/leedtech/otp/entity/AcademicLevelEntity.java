package org.leedtech.otp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.leedtech.otp.utils.commons.BaseEntity;

/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academic_level")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AcademicLevelEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Double fees;
}

