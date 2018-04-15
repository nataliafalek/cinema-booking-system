package com.faleknatalia.cinemaBookingSystem.util;

import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    @Autowired
    TicketPriceRepository ticketPriceRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

    public TicketData findMovie(long reservationId) {


        Reservation reservation = reservationRepository.findOne(reservationId);

        long chosenMovie = reservation.getChosenMovieId();
        ScheduledMovie movie = scheduledMovieRepository.findOne(chosenMovie);
        LocalDateTime movieProjection = movie.getDateOfProjection();
        String projectionDate = movieProjection.format(formatter);
        String projectionHour = movieProjection.format(formatterHour);

        String movieTitle = movieRepository.findOne(movie.getMovieId()).getTitle();
        long cinemaHall = movie.getCinemaHallId();


        List<Seat> seats = seatRepository.findAll(reservation.getChosenSeatId());
//        List<Integer> chosenSeats = new ArrayList<Integer>();
//        seats.stream().map(s -> chosenSeats.add(s.getSeatNumber())).collect(Collectors.toList());

        List<SeatReservationByScheduledMovie> chosenSeats = seatReservationByScheduledMovieRepository
                .findBySeatSeatIdInAndScheduledMovieId(reservation.getChosenSeatId(), reservation.getChosenMovieId());
        List<Integer> ticketPrices = new ArrayList<>();
        chosenSeats.stream().map(s -> ticketPrices.add(ticketPriceRepository.findOne(s.getTicketPriceId()).getTicketValue())).collect(Collectors.toList());

        return new TicketData(movieTitle, projectionDate, projectionHour, cinemaHall, seats, ticketPrices);
    }

    public TicketData findMovie(long chosenMovie, List<Long> seatsIds, List<Integer> ticketPrices ) {
        ScheduledMovie movie = scheduledMovieRepository.findOne(chosenMovie);
        LocalDateTime movieProjection = movie.getDateOfProjection();
        String projectionDate = movieProjection.format(formatter);
        String projectionHour = movieProjection.format(formatterHour);

        String movieTitle = movieRepository.findOne(movie.getMovieId()).getTitle();
        long cinemaHall = movie.getCinemaHallId();
        List<Seat> seats = new ArrayList<>();
//        seatsIds.stream().map(s -> Math.toIntExact(s)).collect(Collectors.toList());
       seatsIds.stream().map(s -> seats.add(seatRepository.findOne(s))).collect(Collectors.toList());

        List<SeatReservationByScheduledMovie> chosenSeats = seatReservationByScheduledMovieRepository
                .findBySeatSeatIdInAndScheduledMovieId(seatsIds, chosenMovie);
//        List<Integer> ticketPrices = new ArrayList<>();
//        chosenSeats.stream().map(s -> ticketPrices.add(ticketPriceRepository.findOne(s.getTicketPriceId()).getTicketValue())).collect(Collectors.toList());

        return new TicketData(movieTitle, projectionDate, projectionHour, cinemaHall, seats, ticketPrices);
    }

}
