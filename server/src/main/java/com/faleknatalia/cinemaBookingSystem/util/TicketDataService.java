package com.faleknatalia.cinemaBookingSystem.util;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.model.*;
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

    public TicketData findMovie(String extOrderId) {
        Reservation reservation = reservationRepository.findByReservationId(extOrderId);
        long chosenMovie = reservation.getChosenMovieId();
        ScheduledMovie movie = scheduledMovieRepository.findOne(chosenMovie);
        LocalDateTime movieProjection = movie.getDateOfProjection();
        String projectionDate = movieProjection.format(formatter);
        String projectionHour = movieProjection.format(formatterHour);

        String movieTitle = movieRepository.findOne(movie.getMovieId()).getTitle();
        long cinemaHall = movie.getCinemaHallId();
        List<SeatAndPriceDetails> seatAndPriceDetails = new ArrayList<>();
        List<ChosenSeatAndPrice> chosenSeatsAndPrices = reservation.getChosenSeatsAndPrices();
        chosenSeatsAndPrices.stream().map(chosenSeatAndPrice ->
                seatAndPriceDetails.add(new SeatAndPriceDetails(
                        seatRepository.findOne(chosenSeatAndPrice.getSeatId()),
                        ticketPriceRepository.findOne(chosenSeatAndPrice.getTicketPriceId())))
        ).collect(Collectors.toList());
        return new TicketData(movieTitle, projectionDate, projectionHour, cinemaHall, seatAndPriceDetails);
    }

    public TicketData findMovie(long chosenMovie, List<ChosenSeatAndPrice> chosenSeatsAndPrices) {
        ScheduledMovie movie = scheduledMovieRepository.findOne(chosenMovie);
        LocalDateTime movieProjection = movie.getDateOfProjection();
        String projectionDate = movieProjection.format(formatter);
        String projectionHour = movieProjection.format(formatterHour);

        String movieTitle = movieRepository.findOne(movie.getMovieId()).getTitle();
        long cinemaHall = movie.getCinemaHallId();
        List<SeatAndPriceDetails> seatAndPriceDetails = new ArrayList<>();
        chosenSeatsAndPrices.stream().map(chosenSeatAndPrice ->
                seatAndPriceDetails.add(
                        new SeatAndPriceDetails(seatRepository.findOne(chosenSeatAndPrice.getSeatId()),
                                ticketPriceRepository.findOne(chosenSeatAndPrice.getTicketPriceId())))
        ).collect(Collectors.toList());
        return new TicketData(movieTitle, projectionDate, projectionHour, cinemaHall, seatAndPriceDetails);
    }

}
