package by.lupach.patientaccountingsystemrestapiserver.restControllers;

import by.lupach.patientaccountingsystemrestapiserver.entities.Patient;
import by.lupach.patientaccountingsystemrestapiserver.entities.Transfer;
import by.lupach.patientaccountingsystemrestapiserver.entities.Ward;
import by.lupach.patientaccountingsystemrestapiserver.services.PatientService;
import by.lupach.patientaccountingsystemrestapiserver.services.TransferService;
import by.lupach.patientaccountingsystemrestapiserver.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferRestController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private WardService wardService;
    @Autowired
    private PatientService patientService;


    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable("id") int id) {
        Optional<Transfer> transferOptional = transferService.findById(id);

        return transferOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Получить все переводы с пагинацией
    @GetMapping
    public Page<Transfer> listTransfers(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return transferService.getAll(page, size).get();
    }

    // Поиск переводов по номеру палаты или имени пациента
    @GetMapping("/search")
    public Page<Transfer> searchTransfers(
            @RequestParam(value = "wardNumber", required = false) String wardNumber,
            @RequestParam(value = "patientName", required = false) String patientName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return transferService.searchByWardNumberOrPatientName(wardNumber, patientName, PageRequest.of(page, size)).get();
    }

    // Создание нового перевода
    @PostMapping("/create-transfer")
    public Transfer createTransfer(@RequestBody Object transferObject) {
        Transfer transfer = new Transfer();
        Map transferMap = (Map) transferObject;
        Map wardMap = (Map) transferMap.get("ward");
        Map patientMap = (Map) transferMap.get("patient");
        Optional<Ward> ward = wardService.getById((Integer) wardMap.get("id"));
        Optional<Patient> patient = patientService.getById((Integer) patientMap.get("id"));
        transfer.setDate(Date.valueOf(transferMap.get("date").toString()));
        transfer.setWard(ward.get());
        transfer.setPatient(patient.get());

        return transferService.save(transfer);
    }

    // Удаление перевода
    @DeleteMapping("/delete-transfer/{id}")
    public void deleteTransfer(@PathVariable("id") Integer id) {
        transferService.deleteById(id);
    }

    // Обновление перевода
    @PutMapping("/{id}")
    public Transfer updateTransfer(@PathVariable("id") Integer id, @RequestBody Object transferObject) {
        Transfer transferToUpdate = transferService.findById(id).get();
        Map transferMap = (Map) transferObject;
        Map wardMap = (Map) transferMap.get("ward");
        Optional<Ward> ward = wardService.getById((Integer) wardMap.get("id"));
        transferToUpdate.setDate(Date.valueOf(transferMap.get("date").toString()));
        transferToUpdate.setWard(ward.get());

        return transferService.save(transferToUpdate);
    }
}
