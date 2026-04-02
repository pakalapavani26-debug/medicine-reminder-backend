package com.medicinereminder.dto;

import com.medicinereminder.entity.Reminder.ReminderStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderDTO {

    private Long id;
    private Long medicineId;
    private String medicineName;
    private String dosage;
    private LocalDateTime reminderTime;
    private ReminderStatus status;
    private String notes;
    private LocalDateTime takenAt;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
}
