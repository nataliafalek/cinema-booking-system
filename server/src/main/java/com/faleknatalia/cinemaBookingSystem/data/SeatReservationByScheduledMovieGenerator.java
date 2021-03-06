package com.faleknatalia.cinemaBookingSystem.data;

import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.CinemaHallRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.TicketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatReservationByScheduledMovieGenerator {

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private TicketPriceRepository ticketPriceRepository;


    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    public List<SeatReservationByScheduledMovie> generateSeatsReservationByScheduledMovies() {
        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAll();
        List<SeatReservationByScheduledMovie> seatReservationByScheduledMovies =
                scheduledMovies.stream().flatMap(scheduledMovie -> {
                    return cinemaHallRepository.findOne(scheduledMovie.getCinemaHallId()).getSeats().stream().map(seat ->
                            newSeat(scheduledMovie, seat)
                    );
                }).collect(Collectors.toList());
        return seatReservationByScheduledMovies;
    }

    private SeatReservationByScheduledMovie newSeat(ScheduledMovie scheduledMovie, Seat seat) {
        return new SeatReservationByScheduledMovie(scheduledMovie.getScheduledMovieId(), scheduledMovie.getCinemaHallId(), true, ticketPriceRepository.findAll().get(0).getTicketPriceId(), seat);
    }
}