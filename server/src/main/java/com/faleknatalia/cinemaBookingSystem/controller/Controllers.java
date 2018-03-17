package com.faleknatalia.cinemaBookingSystem.controller;

import com.faleknatalia.cinemaBookingSystem.mail.EmailSender;
import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.AccessToken;
import com.faleknatalia.cinemaBookingSystem.payment.OrderResponse;
import com.faleknatalia.cinemaBookingSystem.payment.PaymentService;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import com.faleknatalia.cinemaBookingSystem.util.TicketData;
import com.faleknatalia.cinemaBookingSystem.util.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.util.TicketGeneratorPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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

    @Autowired
    EmailSender emailSender;

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

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<>(movieRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/whatsOn/{chosenMovieId}", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovie>> getWhatsOnByMovie(@PathVariable long chosenMovieId) {
        return new ResponseEntity<>(scheduledMovieRepository.findAllByMovieId(chosenMovieId), HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/seats", method = RequestMethod.GET)
    public ResponseEntity<List<SeatReservationByScheduledMovie>> cinemaHallSeatsState(@RequestParam long scheduledMovieId) {
        return new ResponseEntity<>(seatReservationByScheduledMovieRepository.findAllByScheduledMovieId(scheduledMovieId), HttpStatus.OK);
    }

    //to musi byc POST
    @Transactional
    @RequestMapping(value = "/cinemaHall/seats/choose", method = RequestMethod.GET)
    public ResponseEntity<List<SeatReservationByScheduledMovie>> chosenSeat(@RequestParam long scheduledMovieId, @RequestParam List<Long> seatId) {
        seatReservationByScheduledMovieRepository.setFalseForChosenSeat(seatId, scheduledMovieId);
        return new ResponseEntity<>(seatReservationByScheduledMovieRepository.findBySeatIdInAndScheduledMovieId(seatId, scheduledMovieId), HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/addPerson", method = RequestMethod.POST)
    public ResponseEntity<Long> addPerson(@RequestBody PersonalDataAndReservationInfo reservationInfo) {
        PersonalData personalData = new PersonalData(reservationInfo.getName(), reservationInfo.getSurname(), reservationInfo.getPhoneNumber(), reservationInfo.getEmail());
        personalDataRepository.save(personalData);
        //TODO ZAMIENIC SEATID na LIST
        Reservation reservation = new Reservation(reservationInfo.getChosenMovie(), personalData.getPersonId(), reservationInfo.getChosenSeatId());
        reservationRepository.save(reservation);
        return new ResponseEntity<>(reservation.getReservationId(), HttpStatus.OK);
    }

    @RequestMapping(value = "/reservationSummary/{reservationId}", method = RequestMethod.GET)
    public ResponseEntity<ReservationSummary> reservationSummary(@PathVariable long reservationId) {
        TicketData ticketData = ticketDataService.findMovie(reservationId);
        long personalDataId = reservationRepository.findOne(reservationId).getPersonalDataId();
        PersonalData personalData = personalDataRepository.findOne(personalDataId);
        ReservationSummary reservationSummary = new ReservationSummary(ticketData, personalData);
        return new ResponseEntity<>(reservationSummary, HttpStatus.OK);
    }


    //TODO POST
    @RequestMapping(value = "/payment/{reservationId}", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> redirectToPayment(HttpServletResponse response, @PathVariable long reservationId) {

        AccessToken accessToken = paymentService.generateAccessToken(clientId, clientSecret);
        return new ResponseEntity<>(paymentService.generateOrder(accessToken, reservationId, clientId), HttpStatus.OK);

    }

    //todo POST
    @RequestMapping(value = "/sendEmail/{reservationId}", method = RequestMethod.GET)
    public void sendEmail(@PathVariable long reservationId) throws Exception {
        ByteArrayOutputStream doc = new TicketGeneratorPdf().generateTicket(ticketDataService.findMovie(reservationId));
        long personalDataId = reservationRepository.findOne(reservationId).getPersonalDataId();
        String email = personalDataRepository.findOne(personalDataId).getEmail();
        emailSender.sendEmail(email, "NatiCinema cinema ticket", "Please take this ticket and show before projection of the movie.", doc);
    }


//    private void addPdfToResponse(HttpServletResponse response, ByteArrayOutputStream doc) throws Exception {
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=\"file.pdf\"");
//        OutputStream os = response.getOutputStream();
//        doc.writeTo(os);
//        os.flush();
//        os.close();
//    }

}
