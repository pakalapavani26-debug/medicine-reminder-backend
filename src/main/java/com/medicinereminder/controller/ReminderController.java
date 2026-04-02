package com.medicinereminder.controller;

import com.medicinereminder.dto.ReminderDTO;
import com.medicinereminder.entity.Reminder;
import com.medicinereminder.entity.Reminder.ReminderStatus;
import com.medicinereminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping("/medicine/{medicineId}")
    public ResponseEntity<Reminder> createReminder(
            @PathVariable Long medicineId,
            @RequestBody Reminder reminder) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reminderService.createReminder(medicineId, reminder));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReminderDTO>> getRemindersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reminderService.getRemindersByUser(userId));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<ReminderDTO>> getRemindersByStatus(
            @PathVariable Long userId,
            @PathVariable ReminderStatus status) {
        return ResponseEntity.ok(reminderService.getRemindersByUserAndStatus(userId, status));
    }

    @GetMapping("/user/{userId}/today")
    public ResponseEntity<List<ReminderDTO>> getTodayReminders(@PathVariable Long userId) {
        return ResponseEntity.ok(reminderService.getTodayReminders(userId));
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<ReminderDTO>> getRemindersByMedicine(@PathVariable Long medicineId) {
        return ResponseEntity.ok(reminderService.getRemindersByMedicine(medicineId));
    }

    @PatchMapping("/{id}/taken")
    public ResponseEntity<Reminder> markAsTaken(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.markAsTaken(id));
    }

    @PatchMapping("/{id}/missed")
    public ResponseEntity<Reminder> markAsMissed(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.markAsMissed(id));
    }

    @PatchMapping("/{id}/snooze")
    public ResponseEntity<Reminder> snoozeReminder(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") int minutes) {
        return ResponseEntity.ok(reminderService.snoozeReminder(id, minutes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }
}
