package by.lupach.patientaccountingsystemrestapiserver.repositories;

import by.lupach.patientaccountingsystemrestapiserver.entities.AdmissionDischargeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionDischargeHistoryRepository extends JpaRepository<AdmissionDischargeHistory, Integer> {
    List<AdmissionDischargeHistory> findByPatientId(Integer patientId);
}
