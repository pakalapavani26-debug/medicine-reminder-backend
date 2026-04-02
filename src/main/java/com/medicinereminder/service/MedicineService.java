package com.medicinereminder.service;

import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.User;
import com.medicinereminder.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final UserService userService;

    public Medicine addMedicine(Long userId, Medicine medicine) {
        User user = userService.getUserById(userId);

        medicine.setUser(user);

        if (medicine.getIsActive() == null) {
            medicine.setIsActive(true);
        }

        return medicineRepository.save(medicine);
    }

    public List<Medicine> getMedicinesByUser(Long userId) {
        return medicineRepository.findByUserId(userId);
    }

    public List<Medicine> getActiveMedicinesByUser(Long userId) {
        return medicineRepository.findByUserIdAndIsActive(userId, true);
    }

    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
    }

    public Medicine updateMedicine(Long id, Medicine updated) {
        Medicine existing = getMedicineById(id);

        existing.setName(updated.getName());
        existing.setDosage(updated.getDosage());
        existing.setDescription(updated.getDescription());
        existing.setFrequency(updated.getFrequency());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());

        if (updated.getIsActive() != null) {
            existing.setIsActive(updated.getIsActive());
        }

        return medicineRepository.save(existing);
    }

    // ✅ ADD THESE BACK
    public void deleteMedicine(Long id) {
        Medicine medicine = getMedicineById(id);
        medicineRepository.delete(medicine);
    }

    public Medicine toggleActive(Long id) {
        Medicine medicine = getMedicineById(id);
        medicine.setIsActive(!medicine.getIsActive());
        return medicineRepository.save(medicine);
    }
}