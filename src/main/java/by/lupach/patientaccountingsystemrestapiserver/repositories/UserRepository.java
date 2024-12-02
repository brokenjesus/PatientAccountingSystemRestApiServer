package by.lupach.patientaccountingsystemrestapiserver.repositories;

import by.lupach.patientaccountingsystemrestapiserver.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    Page<User> findAll(Pageable pageable);
}