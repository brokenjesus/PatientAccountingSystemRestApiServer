package by.lupach.patientaccountingsystemrestapiserver.restControllers;

import by.lupach.patientaccountingsystemrestapiserver.entities.Ward;
import by.lupach.patientaccountingsystemrestapiserver.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wards")
public class WardRestController {
    @Autowired
    private WardService wardService;

    // Создание новой палаты
    @PostMapping
    public Ward createWard(@RequestBody Ward ward) {
        return wardService.save(ward);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Ward>> getAvailableWards() {
        Optional<List<Ward>> wards = wardService.getAvailableWards();

        return wards
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Получение всех палат с пагинацией
    @GetMapping
    public Page<Ward> listWards(@RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return wardService.getAll(page, size).get();
    }

    // Поиск по номеру палаты
    @GetMapping("/search")
    public Page<Ward> searchByNumber(@RequestParam("number") String number,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return wardService.getByNumber(number, PageRequest.of(page, size)).get();
    }

    // Редактирование палаты
    @PutMapping("/{id}")
    public Ward updateWard(@PathVariable("id") Integer id, @RequestBody Ward ward) {
        ward.setId(id);
        ward.setOccupiedBeds(wardService.getById(id).get().getOccupiedBeds());
        return wardService.save(ward);
    }

    // Удаление палаты
    @DeleteMapping("/{id}")
    public void deleteWard(@PathVariable("id") Integer id) {
        wardService.deleteById(id);
    }

    // Получение конкретной палаты
    @GetMapping("/{id}")
    public Ward getWard(@PathVariable("id") Integer id) {
        Optional<Ward> ward = wardService.getById(id);
        return ward.orElse(null); // Возвращаем null, если палата не найдена
    }
}
