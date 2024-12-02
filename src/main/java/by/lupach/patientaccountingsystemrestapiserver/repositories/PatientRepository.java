package by.lupach.patientaccountingsystemrestapiserver.repositories;

import by.lupach.patientaccountingsystemrestapiserver.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findAll();

    @Query(value = "CALL GetAdmittedPatients()", nativeQuery = true)
    List<Patient> getAdmittedPatients();

    Page<Patient> findAll(Pageable pageable);

    //    List<Patient> findAvailableToTransferPatients();
//    @Query(value = "CALL get_patients_without_transfers()", nativeQuery = true)
    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);
}