package by.lupach.patientaccountingsystemrestapiserver.services;

import by.lupach.patientaccountingsystemrestapiserver.entities.User;
import by.lupach.patientaccountingsystemrestapiserver.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if (user.isEmpty()) {
            logger.warn("User with username '{}' not found", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        logger.info("User found: {}", username);
        return user.get();
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
        logger.info("User saved successfully: {}", user.getUsername());
    }

    @Transactional
    public void deleteById(Integer id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.info("User with ID: {} deleted successfully", id);
    }

    @Transactional
    public Optional<Page<User>> getAll(int page, int size) {
        logger.info("Retrieving users with pagination (Page: {}, Size: {})", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(userRepository.findAll(pageable));
    }
}
