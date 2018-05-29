package com.faleknatalia.cinemaBookingSystem.ticket;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketDataService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

    public TicketData findMovie(String extOrderId) {
        Reservation reservation = reservationRepository.findByReservationId(extOrderId);
        return findMovie(reservation.getChosenMovieId(), reservation.getChosenSeatsAndPrices());
    }

    public TicketData findMovie(long chosenMovie, List<ChosenSeatAndPrice> chosenSeatsAndPrices) {
        ScheduledMovie movie = scheduledMovieRepository.findOne(chosenMovie);
        LocalDateTime movieProjection = movie.getDateOfProjection();
        String projectionDate = movieProjection.format(formatter);
        String projectionHour = movieProjection.format(formatterHour);

        String movieTitle = movieRepository.findOne(movie.getMovieId()).getTitle();
        long cinemaHall = movie.getCinemaHallId();
        List<SeatAndPriceDetails> seatAndPriceDetails = chosenSeatsAndPrices.stream().map(chosenSeatAndPrice ->
                new SeatAndPriceDetails(
                        seatRepository.findOne(chosenSeatAndPrice.getSeatId()),
                        ticketPriceRepository.findOne(chosenSeatAndPrice.getTicketPriceId()))
        ).collect(Collectors.toList());
        return new TicketData(movieTitle, projectionDate, projectionHour, cinemaHall, seatAndPriceDetails);
    }

}
