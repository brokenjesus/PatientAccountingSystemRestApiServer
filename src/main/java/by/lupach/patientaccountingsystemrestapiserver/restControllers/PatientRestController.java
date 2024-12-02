package by.lupach.patientaccountingsystemrestapiserver.restControllers;

import by.lupach.patientaccountingsystemrestapiserver.dto.PatientCustomSearchQueries;
import by.lupach.patientaccountingsystemrestapiserver.entities.AdmissionDischargeHistory;
import by.lupach.patientaccountingsystemrestapiserver.entities.Patient;
import by.lupach.patientaccountingsystemrestapiserver.services.PatientCustomSearchQueriesService;
import by.lupach.patientaccountingsystemrestapiserver.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientRestController {
    Logger logger = Logger.getLogger(PatientRestController.class.getName());

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientCustomSearchQueriesService patientCustomSearchQueriesService;

    @PostMapping
    public ResponseEntity<Void> createPatient(@RequestBody Patient patient) {
        patientService.save(patient);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<Patient> getPatients(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return patientService.getAll(page, size).get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") int id) {
        Optional<Patient>  patient = patientService.getById(id);
        return patient
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admitted")
    public ResponseEntity<List<Patient>> getAdmittedPatientById() {
        Optional<List<Patient>> patients = patientService.getAdmittedPatients();
        List<Patient> result = new ArrayList<>();

        for (Patient patient : patients.get()) {
            result.add(patient);
        }

        return Optional.of(result)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePatient(@PathVariable("id") int id, @RequestBody Patient patient) {
        logger.info("Updating patient with id: " + id);
        logger.info("Received patient data: " + patient);
        patient.setId(id);
        patientService.save(patient);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") int id) {
        logger.info("Deleting patient with id: " + id);
        patientService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/search")
    public Page<Patient> searchPatients(@RequestParam String name, @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return patientService.getByNameContaining(name, page, size).orElse(Page.empty());
    }

    @GetMapping("/custom-query/get-patient-ward")
    public List<PatientCustomSearchQueries> getPatientWardAndPhoneByName(@RequestParam String name) {
        return patientCustomSearchQueriesService.getPatientWardAndPhoneByName(name).orElse(List.of());
    }

    @GetMapping("/custom-query/get-patients-by-date")
    public List<PatientCustomSearchQueries> getPatientsByDate(@RequestParam Date date) {
        return patientCustomSearchQueriesService.getPatientsByDate(date).orElse(List.of());
    }

    @GetMapping("/custom-query/get-female-by-age")
    public List<PatientCustomSearchQueries> getFemalePatientsByAge(@RequestParam int age) {
        return patientCustomSearchQueriesService.getFemalePatientsByAge(age).orElse(List.of());
    }
}
