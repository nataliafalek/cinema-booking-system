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
    @Query("update SeatReservationByScheduledMovie s set s.isFree = false where s.seatId = ?1 and s.scheduledMovieId = ?2")
    int setFalseForChosenSeat(long seatId, long scheduledMovieId);

    List<SeatReservationByScheduledMovie> findAllByScheduledMovieId(long scheduledMovieId);

    SeatReservationByScheduledMovie findOneBySeatIdAndScheduledMovieId(long seatId, long scheduledMovieId);
}
