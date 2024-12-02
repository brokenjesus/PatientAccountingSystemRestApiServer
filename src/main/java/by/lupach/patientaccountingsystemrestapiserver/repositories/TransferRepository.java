package by.lupach.patientaccountingsystemrestapiserver.repositories;

import by.lupach.patientaccountingsystemrestapiserver.entities.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    //    List<Transfer> findAllByFromAccount_IdOrderByFromAccount_Id(Integer fromAccountId);
    List<Transfer> findAll();

    Transfer findByDate(Date date);

    Transfer findTopByPatientIdOrderByDateDesc(int patientId);

    Page<Transfer> findAll(Pageable pageable);

    //    @Query(value = "CALL SearchTransfersByWardNumberAndPatientName()", nativeQuery = true)
    @Query("""
                SELECT t FROM Transfer t
                JOIN t.ward w
                JOIN t.patient p
                WHERE (:wardNumber IS NULL OR w.number LIKE CONCAT('%', :wardNumber, '%'))
                AND (:patientName IS NULL OR p.name LIKE CONCAT('%', :patientName, '%'))
            """)
    Page<Transfer> findByOptionalWardNumberContainingAndOptionalPatientNameContaining(@Param("wardNumber") String wardNumber,
                                                                                      @Param("patientName") String patientName,
                                                                                      Pageable pageable);
}
