package by.lupach.patientaccountingsystemrestapiserver.services;

import by.lupach.patientaccountingsystemrestapiserver.dto.PatientCustomSearchQueries;
import by.lupach.patientaccountingsystemrestapiserver.repositories.PatientCustomSearchQueriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
public class PatientCustomSearchQueriesService {

    private static final Logger logger = LoggerFactory.getLogger(PatientCustomSearchQueriesService.class);

    @Autowired
    private PatientCustomSearchQueriesRepository patientCustomSearchQueriesRepository;

    public Optional<List<PatientCustomSearchQueries>> getPatientWardAndPhoneByName(String patientName) {
        logger.info("Fetching patient ward and phone details for patient name: {}", patientName);
        List<Object[]> results = patientCustomSearchQueriesRepository.getPatientWardAndPhoneByName(patientName);
        Optional<List<PatientCustomSearchQueries>> patientList = convertToWardAndPhoneDto(results);
        logger.info("Found {} records for patient name: {}", patientList.map(List::size).orElse(0), patientName);
        return patientList.map(this::filterByLastTransfer);
    }

    public Optional<List<PatientCustomSearchQueries>> getPatientsByDate(Date specifiedDate) {
        logger.info("Fetching patients for date: {}", specifiedDate);
        List<Object[]> results = patientCustomSearchQueriesRepository.getPatientsByDate(specifiedDate);
        Optional<List<PatientCustomSearchQueries>> patientList = convertToPatientsByDateDto(results, specifiedDate);
        logger.info("Found {} records for specified date: {}", patientList.map(List::size).orElse(0), specifiedDate);
        return patientList.map(this::filterByLastTransfer);
    }

    public Optional<List<PatientCustomSearchQueries>> getFemalePatientsByAge(int specifiedAge) {
        logger.info("Fetching female patients older than or equal to age: {}", specifiedAge);
        List<Object[]> results = patientCustomSearchQueriesRepository.getFemalePatientsByAge(specifiedAge);
        Optional<List<PatientCustomSearchQueries>> patientList = convertToFemalePatientsDto(results);
        logger.info("Found {} female patients older than or equal to age: {}", patientList.map(List::size).orElse(0), specifiedAge);
        return patientList.map(this::filterByLastTransfer);
    }

    // Метод для фильтрации: сохраняем только последний перевод каждого пациента
    private List<PatientCustomSearchQueries> filterByLastTransfer(List<PatientCustomSearchQueries> patients) {
        logger.debug("Filtering patients by last transfer...");
        if (patients == null || patients.isEmpty()) {
            logger.debug("No patients to filter.");
            return Collections.emptyList();
        }

        Map<String, PatientCustomSearchQueries> patientMap = new HashMap<>();
        for (PatientCustomSearchQueries patient : patients) {
            patientMap.merge(
                    patient.getPatientName(),
                    patient,
                    (existing, newEntry) -> (newEntry.getTransferDate() != null &&
                            (existing.getTransferDate() == null || newEntry.getTransferDate().after(existing.getTransferDate())))
                            ? newEntry
                            : existing
            );
        }

        logger.debug("Filtered patients count: {}", patientMap.size());
        return new ArrayList<>(patientMap.values());
    }

    // Уникальный метод преобразования для просмотра телефона и номера палаты
    private Optional<List<PatientCustomSearchQueries>> convertToWardAndPhoneDto(List<Object[]> results) {
        if (results == null) {
            logger.warn("No results found for ward and phone details.");
            return Optional.empty();
        }

        List<PatientCustomSearchQueries> patientList = new ArrayList<>();
        for (Object[] row : results) {
            String patientName = (String) row[0];
            Integer age = (Integer) row[1];
            String phone = (String) row[2];
            String wardNumber = (String) row[3];

            patientList.add(new PatientCustomSearchQueries(patientName, age, wardNumber, phone, null, null)); // admissionDate = null
        }
        logger.info("Converted {} records to Ward and Phone DTO.", patientList.size());
        return Optional.of(patientList);
    }

    // Уникальный метод преобразования для списка больных на заданное число
    private Optional<List<PatientCustomSearchQueries>> convertToPatientsByDateDto(List<Object[]> results, Date specifiedDate) {
        if (results == null) {
            logger.warn("No results found for patients on specified date: {}", specifiedDate);
            return Optional.empty();
        }

        // Хранение только последней подходящей записи для каждого пациента
        Map<String, PatientCustomSearchQueries> patientMap = new HashMap<>();

        for (Object[] row : results) {
            String patientName = (String) row[0];
            Integer age = (Integer) row[1];
            Date transferDate = (Date) row[2];
            String wardNumber = (String) row[3];

            // Учитываем только записи с датой меньше либо равной указанной
            if (transferDate != null && !transferDate.after(specifiedDate)) {
                PatientCustomSearchQueries currentPatient = new PatientCustomSearchQueries(
                        patientName, age, wardNumber, null, null, transferDate
                );

                // Обновляем карту только если дата более поздняя
                patientMap.merge(patientName, currentPatient, (existing, newEntry) ->
                        (existing.getTransferDate() == null || newEntry.getTransferDate().after(existing.getTransferDate()))
                                ? newEntry
                                : existing
                );
            }
        }

        logger.info("Converted {} records to Patients by Date DTO.", patientMap.size());
        return Optional.of(new ArrayList<>(patientMap.values()));
    }

    // Уникальный метод преобразования для женщин, достигших заданного возраста
    private Optional<List<PatientCustomSearchQueries>> convertToFemalePatientsDto(List<Object[]> results) {
        if (results == null) {
            logger.warn("No results found for female patients.");
            return Optional.empty();
        }

        List<PatientCustomSearchQueries> patientList = new ArrayList<>();
        for (Object[] row : results) {
            String patientName = (String) row[0];
            Integer age = (Integer) row[1];
            Date admissionDate = (Date) row[2];

            patientList.add(new PatientCustomSearchQueries(patientName, age, null, null, admissionDate, null)); // wardNumber и phone = null
        }
        logger.info("Converted {} records to Female Patients DTO.", patientList.size());
        return Optional.of(patientList);
    }
}
