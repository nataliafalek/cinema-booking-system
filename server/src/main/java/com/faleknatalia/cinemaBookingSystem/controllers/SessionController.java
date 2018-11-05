package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.dto.SessionParameters;
import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import com.faleknatalia.cinemaBookingSystem.repository.ScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.TicketPriceRepository;
import com.faleknatalia.cinemaBookingSystem.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SessionController {
    static final String DAY_OF_WEEK = LocalDateTime.now().getDayOfWeek().name();

    @Autowired
    private SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public SessionParameters getSessionParameters(HttpSession session) {
        Long chosenMovieId = SessionService.getChosenMovieId(session);
        PersonalData personalData = SessionService.getPersonalData(session);
        List<ChosenSeatAndPrice> extractedChosenSeatsAndPrices = SessionService.getChosenSeatsAndPrices(session);
        String dayOfProjection = !chosenMovieId.equals(0L) ? scheduledMovieRepository.findOne(chosenMovieId).getDateOfProjection().getDayOfWeek().name() : DAY_OF_WEEK;
        List<ChosenSeatAndPrice> chosenSeatsAndPrices = Optional.ofNullable(extractedChosenSeatsAndPrices).orElseGet(ArrayList::new);
        List<Long> chosenSeatsIds = chosenSeatsAndPrices.stream().map(chosenSeatAndPrice -> chosenSeatAndPrice.getSeatId()).collect(Collectors.toList());
        List<SeatReservationByScheduledMovie> chosenSeats = seatReservationByScheduledMovieRepository.findBySeatSeatIdInAndScheduledMovieId(chosenSeatsIds, chosenMovieId);
        List<TicketPrice> ticketPrices =
                chosenSeatsAndPrices.stream().map(chosenSeatAndPrice ->
                        ticketPriceRepository.findOne(chosenSeatAndPrice.getTicketPriceId())).collect(Collectors.toList());
        return new SessionParameters(
                chosenMovieId,
                dayOfProjection,
                chosenSeats,
                ticketPrices,
                personalData);
    }

}
