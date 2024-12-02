package by.lupach.patientaccountingsystemrestapiserver.restControllers;

import by.lupach.patientaccountingsystemrestapiserver.entities.*;
import by.lupach.patientaccountingsystemrestapiserver.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/patient-history")
public class PatientHistoryRestController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private WardService wardService;
    @Autowired
    private AdmissionDischargeHistoryService admissionDischargeHistoryService;

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable("id") int patientId) {
        return patientService.getById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
    }

    @GetMapping("/{id}/history")
    public List<AdmissionDischargeHistory> getPatientHistory(@PathVariable("id") int patientId) {
        return admissionDischargeHistoryService.getByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("No history found for patient ID"));
    }

    @GetMapping("/wards")
    public List<Ward> getAvailableWards() {
        return wardService.getAvailableWards().orElseThrow(() -> new IllegalArgumentException("No wards available"));
    }

    @PostMapping("/{patientId}/{wardId}")
    public ResponseEntity<Void> createPatient(@PathVariable("patientId") Integer patientId,
                                              @RequestBody AdmissionDischargeHistory admissionDischargeHistory,
                                              @PathVariable("wardId") Integer wardId) {
        Patient patient = patientService.getById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
        admissionDischargeHistory.setId(null);
        admissionDischargeHistory.setPatient(patient);
        admissionDischargeHistoryService.save(admissionDischargeHistory);

        if (admissionDischargeHistory.getReason() == Reason.ADMISSION && wardId != null) {
            Transfer transfer = new Transfer();
            transfer.setPatient(patient);
            transfer.setWard(wardService.getById(wardId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ward ID")));
            transfer.setDate(admissionDischargeHistory.getDate());
            transferService.save(transfer);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{patientId}/edit/{historyId}")
    public ResponseEntity<AdmissionDischargeHistory>  editHistory(@PathVariable("patientId") int patientId,
                                                 @PathVariable("historyId") int historyId) {
        Optional<AdmissionDischargeHistory> history = admissionDischargeHistoryService.getById(historyId);
        AdmissionDischargeHistory resultHistory = new AdmissionDischargeHistory();

        resultHistory.setId(historyId);
        resultHistory.setPatient(history.get().getPatient());
        resultHistory.setDate(history.get().getDate());
        resultHistory.setDiagnosis(history.get().getDiagnosis());
        resultHistory.setReason(history.get().getReason());
        resultHistory.setReason(history.get().getReason());

        Optional<AdmissionDischargeHistory> result = Optional.of(resultHistory);
        return result
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{patientId}/edit/{historyId}")
    public ResponseEntity<Void> updateHistory(@PathVariable("patientId") int patientId,
                                              @PathVariable("historyId") int historyId,
                                              @RequestBody AdmissionDischargeHistory history) {
        AdmissionDischargeHistory existingHistory = admissionDischargeHistoryService.getById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid history ID"));

        existingHistory.setDate(history.getDate());
        existingHistory.setDiagnosis(history.getDiagnosis());
        existingHistory.setReason(history.getReason());

        admissionDischargeHistoryService.save(existingHistory);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{patientId}/delete/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable("patientId") int patientId, @PathVariable("historyId") int historyId) {
        admissionDischargeHistoryService.deleteById(historyId);
        return ResponseEntity.ok().build();
    }
}
