package com.medicinereminder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reminder_time")
    private LocalDateTime reminderTime;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status; // PENDING, TAKEN, MISSED, SNOOZED

    private String notes;

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = ReminderStatus.PENDING;
    }

    public enum ReminderStatus {
        PENDING, TAKEN, MISSED, SNOOZED
    }
}
