package by.lupach.patientaccountingsystemrestapiserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ward", uniqueConstraints = {
        @UniqueConstraint(columnNames = "number")
}, indexes = {
        @Index(name = "idx_ward_number", columnList = "number")
})
public class Ward {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String phone;

    @NonNull
    @Column(unique = true, nullable = false)
    private String number;

    @NonNull
    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer bedPlaceCount;

    @Column(name = "occupied_beds", nullable = false)
    @ColumnDefault("0")
    private int occupiedBeds;
}
