package by.lupach.patientaccountingsystemrestapiserver.services;

import by.lupach.patientaccountingsystemrestapiserver.entities.Transfer;
import by.lupach.patientaccountingsystemrestapiserver.repositories.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private TransferRepository transferRepository;

    // Retrieve all transfers
    public Optional<List> getAll() {
        logger.info("Retrieving all transfers");
        return Optional.ofNullable(transferRepository.findAll());
    }

    // Retrieve all transfers with pagination
    public Optional<Page<Transfer>> getAll(int page, int size) {
        logger.info("Retrieving all transfers with pagination (Page: {}, Size: {})", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(transferRepository.findAll(pageable));
    }

    // Find a transfer by ID
    public Optional<Transfer> findById(int id) {
        logger.info("Retrieving transfer with ID: {}", id);
        return transferRepository.findById(id);
    }

    // Search transfers by ward number or patient name
    public Optional<Page<Transfer>> searchByWardNumberOrPatientName(String wardNumber, String patientName, Pageable pageable) {
        logger.info("Searching transfers by ward number: '{}' or patient name: '{}'", wardNumber, patientName);
        return Optional.ofNullable(transferRepository.findByOptionalWardNumberContainingAndOptionalPatientNameContaining(wardNumber, patientName, pageable));
    }

    // Save a transfer
    public Transfer save(Transfer transfer) {
        logger.info("Saving transfer with ID: {}", transfer.getId());
        return transferRepository.save(transfer);
    }

    // Find a transfer by date
    public Optional<Transfer> findByDate(Date date) {
        logger.info("Retrieving transfer for date: {}", date);
        return Optional.ofNullable(transferRepository.findByDate(date));
    }

    // Delete a transfer by ID
    public void deleteById(Integer id) {
        logger.info("Deleting transfer with ID: {}", id);
        transferRepository.deleteById(id);
    }
}
