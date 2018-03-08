package com.faleknatalia.cinemaBookingSystem.controller;

import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.AccessToken;
import com.faleknatalia.cinemaBookingSystem.payment.OrderResponse;
import com.faleknatalia.cinemaBookingSystem.payment.PaymentService;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import com.faleknatalia.cinemaBookingSystem.util.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.util.TicketGeneratorPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

//TODO wydzielic do serwisow logike z controllerow

@RestController
public class Controllers {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Controllers.class);
    private static final String clientId = "322611";
    private static final String clientSecret = "7bf401d342210d73b85081c0a2fae474";

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonalDataRepository personalDataRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TicketDataService ticketDataService;

    @Autowired
    private SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    @Autowired
    private PaymentService paymentService;


    //TODO optymalizacja - wydzielic metode do serwisu osobnego, tak by efektywnie laczyc ScheduledMovie i ScheduledMovieDetails
    @RequestMapping(value = "/whatsOn", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovieDetails>> whatsOn() {

        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAll();
        List<ScheduledMovieDetails> scheduledMovieDetails =
                scheduledMovies.stream().map(
                        sm -> {
                            Movie one = movieRepository.findOne(sm.getMovieId());
                            return new ScheduledMovieDetails(
                                    one.getTitle(),
                                    one.getDurationInMinutes(),
                                    sm.getDateOfProjection(),
                                    sm.getScheduledMovieId()
                            );
                        }).collect(Collectors.toList());

        return new ResponseEntity<>(scheduledMovieDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/seats/{scheduledMovieId}", method = RequestMethod.GET)
    public ResponseEntity<List<SeatReservationByScheduledMovie>> cinemaHallSeatsState(@PathVariable long scheduledMovieId) {
        //  long cinemaHallId = scheduledMovieRepository.findOne(scheduledMovieId).getCinemaHallId();
        return new ResponseEntity<>(seatReservationByScheduledMovieRepository.findAllByScheduledMovieId(scheduledMovieId), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/cinemaHall/seats/choose/{scheduledMovieId}/{seatId}", method = RequestMethod.GET)
    public ResponseEntity<Seat> chosenSeat(@PathVariable long seatId, @PathVariable long scheduledMovieId) {
        seatReservationByScheduledMovieRepository.setFalseForChosenSeat(seatId, scheduledMovieId);
        Seat chosenSeat = seatRepository.findOne(seatId);
        return new ResponseEntity<>(chosenSeat, HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/addPerson", method = RequestMethod.POST)
    public ResponseEntity<Long> addPerson(@RequestBody PersonalDataAndReservationInfo reservationInfo) {
        PersonalData personalData = new PersonalData(reservationInfo.getName(), reservationInfo.getSurname(), reservationInfo.getPhoneNumber(), reservationInfo.getEmail());
        personalDataRepository.save(personalData);
        Reservation reservation = new Reservation(reservationInfo.getChosenMovie(), personalData.getPersonId(), reservationInfo.getChosenSeatId());
        reservationRepository.save(reservation);
        return new ResponseEntity<>(reservation.getReservationId(), HttpStatus.OK);
    }

    @RequestMapping(value = "/generatePdf/{reservationId}", method = RequestMethod.GET)
    public void getPdf(HttpServletResponse response, @PathVariable long reservationId) throws Exception {

        ByteArrayOutputStream doc = new TicketGeneratorPdf().generateTicket(ticketDataService.findMovie(reservationId));

        //void addPdfToResponse(response, doc )
        addPdfToResponse(response, doc);
    }


    //TODO POST
    @RequestMapping(value = "/payment/{reservationId}", method = RequestMethod.POST)
    public void redirectToPayment(HttpServletResponse response, @PathVariable long reservationId) {

        AccessToken accessToken = paymentService.generateAccessToken(clientId, clientSecret);
        OrderResponse order = paymentService.generateOrder(accessToken, reservationId, clientId);

        //redirect
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", order.getRedirectUri());
        response.setHeader("Connection", "close");
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

    private void addPdfToResponse(HttpServletResponse response, ByteArrayOutputStream doc) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"file.pdf\"");
        OutputStream os = response.getOutputStream();
        doc.writeTo(os);
        os.flush();
        os.close();
    }

}
