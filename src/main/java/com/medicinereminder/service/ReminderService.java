package com.medicinereminder.service;

import com.medicinereminder.dto.ReminderDTO;
import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.Reminder;
import com.medicinereminder.entity.Reminder.ReminderStatus;
import com.medicinereminder.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final MedicineService medicineService;

    public Reminder createReminder(Long medicineId, Reminder reminder) {
        Medicine medicine = medicineService.getMedicineById(medicineId);
        reminder.setMedicine(medicine);
        return reminderRepository.save(reminder);
    }

    public List<ReminderDTO> getRemindersByUser(Long userId) {
        return reminderRepository.findByUserId(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ReminderDTO> getRemindersByUserAndStatus(Long userId, ReminderStatus status) {
        return reminderRepository.findByUserIdAndStatus(userId, status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ReminderDTO> getRemindersByMedicine(Long medicineId) {
        return reminderRepository.findByMedicineId(medicineId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ReminderDTO> getTodayReminders(Long userId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        return reminderRepository.findByUserIdAndReminderTimeBetween(userId, startOfDay, endOfDay)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Reminder markAsTaken(Long reminderId) {
        Reminder reminder = getReminderById(reminderId);
        reminder.setStatus(ReminderStatus.TAKEN);
        reminder.setTakenAt(LocalDateTime.now());
        return reminderRepository.save(reminder);
    }

    public Reminder markAsMissed(Long reminderId) {
        Reminder reminder = getReminderById(reminderId);
        reminder.setStatus(ReminderStatus.MISSED);
        return reminderRepository.save(reminder);
    }

    public Reminder snoozeReminder(Long reminderId, int minutes) {
        Reminder reminder = getReminderById(reminderId);
        reminder.setStatus(ReminderStatus.SNOOZED);
        reminder.setReminderTime(reminder.getReminderTime().plusMinutes(minutes));
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));
    }

    // Auto-mark overdue reminders as MISSED every minute
    @Scheduled(fixedRate = 60000)
    public void autoMarkMissed() {
        List<Reminder> overdue = reminderRepository.findOverdueReminders(LocalDateTime.now().minusMinutes(30));
        overdue.forEach(r -> {
            r.setStatus(ReminderStatus.MISSED);
            reminderRepository.save(r);
        });
    }

    private ReminderDTO toDTO(Reminder r) {
        return ReminderDTO.builder()
                .id(r.getId())
                .medicineId(r.getMedicine().getId())
                .medicineName(r.getMedicine().getName())
                .dosage(r.getMedicine().getDosage())
                .reminderTime(r.getReminderTime())
                .status(r.getStatus())
                .notes(r.getNotes())
                .takenAt(r.getTakenAt())
                .createdAt(r.getCreatedAt())
                .userId(r.getMedicine().getUser().getId())
                .userName(r.getMedicine().getUser().getName())
                .build();
    }
}
