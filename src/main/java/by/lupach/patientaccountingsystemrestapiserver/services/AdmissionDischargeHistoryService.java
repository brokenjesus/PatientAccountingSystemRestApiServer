package by.lupach.patientaccountingsystemrestapiserver.services;

import by.lupach.patientaccountingsystemrestapiserver.entities.AdmissionDischargeHistory;
import by.lupach.patientaccountingsystemrestapiserver.repositories.AdmissionDischargeHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdmissionDischargeHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(AdmissionDischargeHistoryService.class);

    @Autowired
    private AdmissionDischargeHistoryRepository historyRepository;

    public Optional<List<AdmissionDischargeHistory>> getByPatientId(Integer patientId) {
        logger.info("Fetching admission/discharge history for patient with ID: {}", patientId);
        Optional<List<AdmissionDischargeHistory>> result = Optional.ofNullable(historyRepository.findByPatientId(patientId));
        logger.info("Found {} history records for patient with ID: {}", result.map(List::size).orElse(0), patientId);
        return result;
    }

    public void save(AdmissionDischargeHistory history) {
        logger.info("Saving admission/discharge history for patient with ID: {}", history.getPatient().getId());
        AdmissionDischargeHistory savedHistory = historyRepository.save(history);
        logger.info("Saved history with ID: {}", savedHistory.getId());
    }

    public Optional<AdmissionDischargeHistory> getById(Integer historyId) {
        logger.info("Fetching admission/discharge history with ID: {}", historyId);
        Optional<AdmissionDischargeHistory> result = Optional.of(historyRepository.getById(historyId));
        logger.info("Found history with ID: {}", historyId);
        return result;
    }

    public void deleteById(Integer id) {
        logger.info("Deleting admission/discharge history with ID: {}", id);
        historyRepository.deleteById(id);
        logger.info("Deleted history with ID: {}", id);
    }
}
