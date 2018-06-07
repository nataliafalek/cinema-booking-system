package com.faleknatalia.cinemaBookingSystem.payment.repository;

import com.faleknatalia.cinemaBookingSystem.payment.NotificationResponseDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationResponseDBRepository extends JpaRepository<NotificationResponseDB, Long> {
}
