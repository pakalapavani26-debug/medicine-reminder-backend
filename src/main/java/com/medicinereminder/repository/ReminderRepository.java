package com.medicinereminder.repository;

import com.medicinereminder.entity.Reminder;
import com.medicinereminder.entity.Reminder.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByMedicineId(Long medicineId);

    List<Reminder> findByStatus(ReminderStatus status);

    @Query("SELECT r FROM Reminder r WHERE r.medicine.user.id = :userId ORDER BY r.reminderTime DESC")
    List<Reminder> findByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Reminder r WHERE r.medicine.user.id = :userId AND r.status = :status ORDER BY r.reminderTime DESC")
    List<Reminder> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ReminderStatus status);

    @Query("SELECT r FROM Reminder r WHERE r.medicine.user.id = :userId AND r.reminderTime BETWEEN :start AND :end ORDER BY r.reminderTime ASC")
    List<Reminder> findByUserIdAndReminderTimeBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT r FROM Reminder r WHERE r.status = 'PENDING' AND r.reminderTime <= :now")
    List<Reminder> findOverdueReminders(@Param("now") LocalDateTime now);
}
