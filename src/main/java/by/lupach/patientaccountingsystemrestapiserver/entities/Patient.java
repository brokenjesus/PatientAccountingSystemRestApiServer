package by.lupach.patientaccountingsystemrestapiserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

enum Gender {
    MALE, FEMALE
}

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patient", uniqueConstraints = {
        @UniqueConstraint(name = "unique_name_age", columnNames = {"name", "age"})
}, indexes = {
        @Index(name = "idx_patient_name", columnList = "name")
})
public class Patient {

    @NonNull
    @Column(nullable = false)
    public Integer height;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NonNull
    @Column(nullable = false)
    private Integer age;

    @NonNull
    @Column(nullable = false)
    private String hairColor;

    private String distinctiveCharacteristics;
}
