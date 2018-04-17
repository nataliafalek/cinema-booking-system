package com.faleknatalia.cinemaBookingSystem.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRequestDBRepository extends JpaRepository<OrderRequestsAndResponseDB, Long> {
}
