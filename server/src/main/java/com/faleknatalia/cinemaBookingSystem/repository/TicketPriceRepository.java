package com.faleknatalia.cinemaBookingSystem.repository;

import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPriceRepository extends JpaRepository<TicketPrice,Long> {
}
