package com.medicinereminder.controller;

import com.medicinereminder.entity.Medicine;
import com.medicinereminder.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Medicine> addMedicine(
            @PathVariable Long userId,
            @RequestBody Medicine medicine) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicineService.addMedicine(userId, medicine));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Medicine>> getMedicinesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(medicineService.getMedicinesByUser(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<Medicine>> getActiveMedicines(@PathVariable Long userId) {
        return ResponseEntity.ok(medicineService.getActiveMedicinesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(
            @PathVariable Long id,
            @RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, medicine));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Medicine> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.toggleActive(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }
}
