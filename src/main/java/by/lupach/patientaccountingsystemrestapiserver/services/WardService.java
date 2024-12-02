package by.lupach.patientaccountingsystemrestapiserver.services;

import by.lupach.patientaccountingsystemrestapiserver.entities.Ward;
import by.lupach.patientaccountingsystemrestapiserver.repositories.WardRepository;
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
public class WardService {

    private static final Logger logger = LoggerFactory.getLogger(WardService.class);

    @Autowired
    private WardRepository wardRepository;

    // Create or update a ward
    public Ward save(Ward ward) {
        logger.info("Saving or updating ward with ID: {}", ward.getId());
        return wardRepository.save(ward);
    }

    // Retrieve all wards
    public Optional<List<Ward>> getAll() {
        logger.info("Retrieving all wards");
        return Optional.ofNullable(wardRepository.findAll());
    }

    // Retrieve a ward by ID
    public Optional<Ward> getById(Integer id) {
        logger.info("Retrieving ward with ID: {}", id);
        return wardRepository.findById(id);
    }

    // Retrieve wards by number (pagination)
    public Optional<Page<Ward>> getByNumber(String number, PageRequest pageRequest) {
        logger.info("Searching for wards by number: {} (Page: {}, Size: {})", number, pageRequest.getPageNumber(), pageRequest.getPageSize());
        return Optional.ofNullable(wardRepository.findByNumberContainingIgnoreCase(number, pageRequest));
    }

    // Delete a ward by ID
    public void deleteById(Integer id) {
        logger.info("Deleting ward with ID: {}", id);
        wardRepository.deleteById(id);
        logger.info("Ward with ID: {} deleted successfully", id);
    }

    // Retrieve all wards with pagination
    public Optional<Page<Ward>> getAll(int page, int size) {
        logger.info("Retrieving all wards with pagination (Page: {}, Size: {})", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(wardRepository.findAll(pageable));
    }

    // Retrieve available wards
    public Optional<List<Ward>> getAvailableWards() {
        logger.info("Retrieving available wards");
        return Optional.ofNullable(wardRepository.findAvailableWards());
    }
}
