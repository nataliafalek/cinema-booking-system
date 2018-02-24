package com.faleknatalia.cinemaBookingSystem.repository;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
}
