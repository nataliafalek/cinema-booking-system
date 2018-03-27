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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
        List<Seat> seats = seatRepository.findAll(reservation.getChosenSeatId());
        List<Integer> chosenSeat = new ArrayList<>();
        seats.stream().map(s -> chosenSeat.add(s.getSeatNumber())).collect(Collectors.toList());
        //TODO podstawic cene z nowej klasy
        int ticketPrice = 10;

        return new TicketData(movieTitle, movieProjection, cinemaHall, chosenSeat, ticketPrice);
    }

    public TicketData findMovie(long chosenMovie, List<Long> seatsIds ) {
        ScheduledMovie movie = scheduledMovieRepository.findOne(chosenMovie);
        LocalDateTime movieProjection = movie.getDateOfProjection();
        String movieTitle = movieRepository.findOne(movie.getMovieId()).getTitle();
        long cinemaHall = movie.getCinemaHallId();
        List<Integer> chosenSeats = new ArrayList<>();
//        seatsIds.stream().map(s -> Math.toIntExact(s)).collect(Collectors.toList());
       seatsIds.stream().map(s -> chosenSeats.add(seatRepository.findOne(s).getSeatNumber())).collect(Collectors.toList());
        //TODO podstawic cene z nowej klasy
        int ticketPrice = 10;

        return new TicketData(movieTitle, movieProjection, cinemaHall, chosenSeats, ticketPrice);
    }

}
