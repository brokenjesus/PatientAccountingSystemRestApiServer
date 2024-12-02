package by.lupach.patientaccountingsystemrestapiserver.restControllers;

import by.lupach.patientaccountingsystemrestapiserver.entities.User;
import by.lupach.patientaccountingsystemrestapiserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {


    private final static int PAGE_SIZE = 20;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Регистрация нового пользователя
    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
    }

    // Поиск пользователя по имени
    @GetMapping("/search")
    public User searchUser(@RequestParam String username) {
        return userService.loadUserByUsername(username);
    }

    // Получить всех пользователей с пагинацией
    @GetMapping("/manage")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page) {
        return userService.getAll(page, PAGE_SIZE).get();
    }

    @GetMapping("/load-user")
    public User loadUser( @RequestParam(required = true) String username, @RequestParam(required = false) String password) {
        return userService.loadUserByUsername(username);
    }

    // Удалить пользователя
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    // Редактировать данные пользователя
    @PostMapping("/edit")
    public void editUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
    }

}
