package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.dto.SessionParameters;
import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.repository.ScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.TicketPriceRepository;
import com.faleknatalia.cinemaBookingSystem.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SessionController {

    @Autowired
    private SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public SessionParameters getSessionParameters(HttpSession session) {
        long chosenMovieId = SessionService.getChosenMovieId(session);
        PersonalData personalData = SessionService.getPersonalData(session);
        List<ChosenSeatAndPrice> extractedChosenSeatsAndPrices = SessionService.getChosenSeatsAndPrices(session);
        String dayOfProjection = scheduledMovieRepository.findOne(chosenMovieId).getDateOfProjection().getDayOfWeek().name();
        List<ChosenSeatAndPrice> chosenSeatsAndPrices = Optional.ofNullable(extractedChosenSeatsAndPrices).orElseGet(ArrayList::new);
        List<Long> chosenSeatsIds = chosenSeatsAndPrices.stream().map(chosenSeatAndPrice -> chosenSeatAndPrice.getSeatId()).collect(Collectors.toList());
        List<SeatReservationByScheduledMovie> chosenSeats = seatReservationByScheduledMovieRepository.findBySeatSeatIdInAndScheduledMovieId(chosenSeatsIds,chosenMovieId);
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
