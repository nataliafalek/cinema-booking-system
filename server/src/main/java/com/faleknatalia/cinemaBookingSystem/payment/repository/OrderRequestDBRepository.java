package com.faleknatalia.cinemaBookingSystem.payment.repository;

import com.faleknatalia.cinemaBookingSystem.payment.OrderRequestsAndResponseDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRequestDBRepository extends JpaRepository<OrderRequestsAndResponseDB, Long> {
}
