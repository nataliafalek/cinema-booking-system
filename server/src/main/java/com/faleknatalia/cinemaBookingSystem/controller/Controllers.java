package com.faleknatalia.cinemaBookingSystem.controller;

import com.faleknatalia.cinemaBookingSystem.mail.EmailSender;
import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.*;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import com.faleknatalia.cinemaBookingSystem.util.TicketData;
import com.faleknatalia.cinemaBookingSystem.util.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.util.TicketGeneratorPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO wydzielic do serwisow logike z controllerow

@RestController
public class Controllers {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Controllers.class);
    private static final String clientId = "322611";
    private static final String clientSecret = "7bf401d342210d73b85081c0a2fae474";
    private static final DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");


    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonalDataRepository personalDataRepository;

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

    @Value("${dev_mode}")
    private boolean devMode;

    //TODO optymalizacja - wydzielic metode do serwisu osobnego, tak by efektywnie laczyc ScheduledMovie i ScheduledMovieDetails
    @RequestMapping(value = "/whatsOn", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovieDetails>> whatsOn() {

        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAllByOrderByDateOfProjection();
        List<ScheduledMovieDetails> scheduledMovieDetails = getScheduledMovieDetails(scheduledMovies);

        return new ResponseEntity<>(scheduledMovieDetails, HttpStatus.OK);
    }


    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<>(movieRepository.findAll(), HttpStatus.OK);
    }

    //SCHEDULEDMOVIE DETAILS
    @RequestMapping(value = "/whatsOn/{chosenMovieId}", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovieDetails>> getWhatsOnByMovie(@PathVariable long chosenMovieId) {
        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAllByMovieId(chosenMovieId);
        List<ScheduledMovieDetails> scheduledMovieDetails = getScheduledMovieDetails(scheduledMovies);
        return new ResponseEntity<>(scheduledMovieDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/seats", method = RequestMethod.GET)
    public ResponseEntity<List<SeatReservationByScheduledMovie>> cinemaHallSeatsState(@RequestParam long scheduledMovieId) {
        return new ResponseEntity<>(seatReservationByScheduledMovieRepository.findAllByScheduledMovieId(scheduledMovieId), HttpStatus.OK);
    }


    @Transactional
    @RequestMapping(value = "/cinemaHall/seats/choose/{scheduledMovieId}", method = RequestMethod.POST)
    public ResponseEntity<List<SeatReservationByScheduledMovie>> chosenSeat(@PathVariable long scheduledMovieId, @RequestBody List<Long> seatId) {
        seatReservationByScheduledMovieRepository.setFalseForChosenSeat(seatId, scheduledMovieId);
        return new ResponseEntity<>(seatReservationByScheduledMovieRepository.findBySeatIdInAndScheduledMovieId(seatId, scheduledMovieId), HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/addPerson", method = RequestMethod.POST)
    public ResponseEntity<Long> addPerson(@RequestBody PersonalDataAndReservationInfo reservationInfo) {
        PersonalData personalData = new PersonalData(reservationInfo.getName(), reservationInfo.getSurname(), reservationInfo.getPhoneNumber(), reservationInfo.getEmail());
        personalDataRepository.save(personalData);
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


    @RequestMapping(value = "/payment/{reservationId}", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> redirectToPayment(HttpServletResponse response, @PathVariable long reservationId) throws Exception {

        AccessToken accessToken = paymentService.generateAccessToken(clientId, clientSecret);

        if (devMode) {
            //KOD DO TESTÓW
            Buyer buyer = new Buyer(
                    "naticinema@gmail.com",
                    "123456789",
                    "Michał",
                    "Abcd"
            );

            PayMethod payMethod = new PayMethod("PBL");
            List<Product> productList = new ArrayList<Product>() {{
                add(new Product("ticket", "10", "1"));
            }};

            OrderResponseNotification orderResponseNotification = new OrderResponseNotification(
                    String.valueOf(reservationId),
                    String.valueOf(reservationId),
                    "http://localhost:8080/notify",
                    "127.0.0.1",
                    clientId,
                    "bilecik test",
                    "PLN",
                    "100",
                    buyer,
                    payMethod,
                    productList,
                    "COMPLETED"
            );

            List<PropertyNotifcation> propertyNotifcationList = new ArrayList<PropertyNotifcation>() {{
                add(new PropertyNotifcation("PAYMENT_ID", "12345"));
            }};
            notify(new NotificationResponse(orderResponseNotification, "2016-03-02T12:58:14.828+01:00", propertyNotifcationList));
        }
        return new ResponseEntity<>(paymentService.generateOrder(accessToken, reservationId, clientId), HttpStatus.OK);
    }


    @RequestMapping(value = "/sendEmail/{reservationId}", method = RequestMethod.POST)
    public void sendEmail(@PathVariable long reservationId) throws Exception {
        ByteArrayOutputStream doc = new TicketGeneratorPdf().generateTicket(ticketDataService.findMovie(reservationId));
        long personalDataId = reservationRepository.findOne(reservationId).getPersonalDataId();
        String email = personalDataRepository.findOne(personalDataId).getEmail();
        emailSender.sendEmail(email, "NatiCinema cinema ticket", "Please take this ticket and show before projection of the movie.", doc);
    }

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void notify(NotificationResponse notificationResponse) throws Exception {

        if (notificationResponse.getOrder().getStatus().equals("COMPLETED")) {
            sendEmail(Long.parseLong(notificationResponse.getOrder().getExtOrderId()));
        }

    }

    private List<ScheduledMovieDetails> getScheduledMovieDetails(List<ScheduledMovie> scheduledMovies) {
        return scheduledMovies.stream().map(
                sm -> {
                    Movie one = movieRepository.findOne(sm.getMovieId());
                    return new ScheduledMovieDetails(
                            one.getTitle(),
                            one.getDurationInMinutes(),
                            sm.getDateOfProjection(),
                            sm.getScheduledMovieId(),
                            sm.getDateOfProjection().getDayOfWeek().name(),
                            sm.getDateOfProjection().format(formatterHour),
                            one.getDescription()
                    );
                }).collect(Collectors.toList());
    }

}
