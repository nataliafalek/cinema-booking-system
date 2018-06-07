package com.faleknatalia.cinemaBookingSystem.repository;

import com.faleknatalia.cinemaBookingSystem.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
//    @Modifying
//    @Query("update Seat s set s.isFree = false where s.seatId = ?1")
//    int reserveSeat(long seatId);

    long countAllBySeatIdIsNotNull();


}
