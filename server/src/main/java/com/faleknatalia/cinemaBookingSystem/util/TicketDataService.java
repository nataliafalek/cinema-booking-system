package com.faleknatalia.cinemaBookingSystem.util;

import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ReservationRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketDataService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    SeatRepository seatRepository;

    public TicketData findMovie(long reservationId) {
        Reservation reservation = reservationRepository.findOne(reservationId);
        long chosenMovie = reservation.getChosenMovieId();
        ScheduledMovie movie = scheduledMovieRepository.findOne(chosenMovie);
        LocalDateTime movieProjection = movie.getDateOfProjection();
        String movieTitle = movieRepository.findOne(movie.getMovieId()).getTitle();
        long cinemaHall = movie.getCinemaHallId();
        Seat seat = seatRepository.findOne(reservation.getChosenSeatId());
        int chosenSeat = seat.getSeatNumber();
        int ticketPrice = seat.getTicketPrice();

        return new TicketData(movieTitle, movieProjection, cinemaHall, chosenSeat, ticketPrice);
    }

}