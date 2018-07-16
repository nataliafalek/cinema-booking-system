package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.dto.ReservationSummaryDto;
import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketData;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.validator.PersonalDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MakeReservationController {

    @Autowired
    private SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    @Autowired
    private TicketDataService ticketDataService;

    @RequestMapping(value = "/cinemaHall/seats", method = RequestMethod.GET)
    public ResponseEntity<List<List<SeatReservationByScheduledMovie>>> seatsByCinemaHallAndMovie(@RequestParam long scheduledMovieId) {
        List<SeatReservationByScheduledMovie> cinemaHallSeats = seatReservationByScheduledMovieRepository.findAllByScheduledMovieId(scheduledMovieId);
        Map<Integer, List<SeatReservationByScheduledMovie>> groupByRow =
                cinemaHallSeats.stream().collect(Collectors.groupingBy(seat -> seat.getSeat().getRowNumber()));
        return new ResponseEntity<>(new ArrayList<>(groupByRow.values()), HttpStatus.OK);
    }


    @Transactional
    @RequestMapping(value = "/cinemaHall/seats/choose/{scheduledMovieId}", method = RequestMethod.POST)
    public ResponseEntity<List<SeatReservationByScheduledMovie>> chosenSeat(HttpSession session, @PathVariable long scheduledMovieId, @RequestBody List<ChosenSeatAndPrice> chosenSeatsAndPrices) {
        List<Long> seatIds = chosenSeatsAndPrices.stream().map(seat -> seat.getSeatId()).collect(Collectors.toList());
        List<SeatReservationByScheduledMovie> seatSeatIdInAndScheduledMovieId = seatReservationByScheduledMovieRepository.findBySeatSeatIdInAndScheduledMovieId(seatIds, scheduledMovieId);
        session.setAttribute("chosenSeatsAndPrices", chosenSeatsAndPrices);
        session.setAttribute("chosenMovieId", scheduledMovieId);
        return new ResponseEntity<>(seatSeatIdInAndScheduledMovieId, HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/addPerson", method = RequestMethod.POST)
    public ResponseEntity<String> createReservation(HttpSession session, @RequestBody PersonalData personalData) {
        Optional<String> validationResult = PersonalDataValidator.validate(personalData);
        if (validationResult.isPresent()) {
            throw new IllegalArgumentException(validationResult.get());
        } else {
            List<ChosenSeatAndPrice> chosenSeatAndPrices = (List<ChosenSeatAndPrice>) session.getAttribute("chosenSeatsAndPrices");
            Reservation reservation = new Reservation((long) session.getAttribute("chosenMovieId"), personalData, chosenSeatAndPrices);
            String reservationId = reservation.getReservationId();
            session.setAttribute("personalData", personalData);
            session.setAttribute("reservation", reservation);
            return new ResponseEntity<>(reservationId, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/reservationSummary", method = RequestMethod.GET)
    public ResponseEntity<ReservationSummaryDto> reservationSummary(HttpSession session) {
        List<ChosenSeatAndPrice> chosenSeatAndPrices = (List<ChosenSeatAndPrice>) session.getAttribute("chosenSeatsAndPrices");
        TicketData ticketData = ticketDataService.findMovie((long) session.getAttribute("chosenMovieId"), chosenSeatAndPrices);
        ReservationSummaryDto reservationSummaryDto = new ReservationSummaryDto(ticketData, (PersonalData) session.getAttribute("personalData"));
        return new ResponseEntity<>(reservationSummaryDto, HttpStatus.OK);
    }


}
