package com.faleknatalia.cinemaBookingSystem.controller;

import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import com.faleknatalia.cinemaBookingSystem.util.TicketData;
import com.faleknatalia.cinemaBookingSystem.util.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.util.TicketGeneratorPdf;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//TODO wydzielic do serwisow logike z controllerow

@RestController
public class Controllers {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Controllers.class);


    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

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

    private void addPdfToResponse(HttpServletResponse response, ByteArrayOutputStream doc) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"file.pdf\"");
        OutputStream os = response.getOutputStream();
        doc.writeTo(os);
        os.flush();
        os.close();
    }

}
