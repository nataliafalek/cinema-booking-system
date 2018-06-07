package com.faleknatalia.cinemaBookingSystem.repository;

import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatReservationByScheduledMovieRepository extends JpaRepository<SeatReservationByScheduledMovie, Long> {

    @Modifying
    @Query("update SeatReservationByScheduledMovie s set s.isFree = false where s.seat.seatId in (?1) and s.scheduledMovieId = ?2")
    int reserveSeat(List<Long> seatId, long scheduledMovieId);

    List<SeatReservationByScheduledMovie> findAllByScheduledMovieId(long scheduledMovieId);

    List<SeatReservationByScheduledMovie> findBySeatSeatIdInAndScheduledMovieId(List<Long> seatId, long scheduledMovieId);
}
