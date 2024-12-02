package by.lupach.patientaccountingsystemrestapiserver.repositories;

import by.lupach.patientaccountingsystemrestapiserver.entities.Ward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findAll();

    Page<Ward> findAll(Pageable pageable);

    @Query(value = "CALL GetAvailableWards()", nativeQuery = true)
    List<Ward> findAvailableWards();

    Page<Ward> findByNumberContainingIgnoreCase(String number, Pageable pageable);
}