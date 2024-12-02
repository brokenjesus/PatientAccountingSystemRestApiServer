package by.lupach.patientaccountingsystemrestapiserver.services;

import by.lupach.patientaccountingsystemrestapiserver.entities.Patient;
import by.lupach.patientaccountingsystemrestapiserver.repositories.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    private PatientRepository patientRepository;

    // Create or update a patient
    public Patient save(Patient patient) {
        logger.info("Saving or updating patient with ID: {}", patient.getId());
        return patientRepository.save(patient);
    }

    // Retrieve all patients
    public Optional<List<Patient>> getAll() {
        logger.info("Retrieving all patients");
        return Optional.ofNullable(patientRepository.findAll());
    }

    // Retrieve a patient by ID
    public Optional<Patient> getById(Integer id) {
        logger.info("Retrieving patient with ID: {}", id);
        return patientRepository.findById(id);
    }

    // Retrieve all patients with pagination
    public Optional<Page<Patient>> getAll(int page, int size) {
        logger.info("Retrieving patients with pagination (Page: {}, Size: {})", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(patientRepository.findAll(pageable));
    }

    // Retrieve all admitted patients
    public Optional<List<Patient>> getAdmittedPatients() {
        logger.info("Retrieving all admitted patients");
        return Optional.ofNullable(patientRepository.getAdmittedPatients());
    }

    // Search for patients by name (case insensitive)
    public Optional<Page<Patient>> getByNameContaining(String name, int page, int size) {
        logger.info("Searching for patients by name (contains): {}", name);
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(patientRepository.findByNameContainingIgnoreCase(name, pageable));
    }

    // Delete a patient by ID
    public void deleteById(Integer id) {
        logger.info("Deleting patient with ID: {}", id);
        patientRepository.deleteById(id);
    }
}
