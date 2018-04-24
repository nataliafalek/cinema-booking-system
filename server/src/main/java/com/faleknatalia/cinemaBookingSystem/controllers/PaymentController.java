package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.mail.EmailSender;
import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.*;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import com.faleknatalia.cinemaBookingSystem.util.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.util.TicketGeneratorPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO wydzielic do serwisow logike z controllerow
@RestController
public class PaymentController {
    private static final String clientId = "322611";
    private static final String clientSecret = "7bf401d342210d73b85081c0a2fae474";


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

    @Autowired
    OrderRequestDBRepository orderRequestDBRepository;

    @Autowired
    TicketPriceRepository ticketPriceRepository;

    @Value("${dev_mode}")
    private boolean devMode;

    @Transactional
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> saveReservationAndRedirectToPayment(HttpServletResponse response, HttpSession session) throws Exception {
        List<ChosenSeatAndPrice> chosenSeatAndPrices = (List<ChosenSeatAndPrice>) session.getAttribute("chosenSeatsAndPrices");
        List<Long> chosenSeatsIds = chosenSeatAndPrices.stream().map(chosenSeatAndPrice -> chosenSeatAndPrice.getSeatId()).collect(Collectors.toList());
        seatReservationByScheduledMovieRepository.setFalseForChosenSeat(chosenSeatsIds, (long) session.getAttribute("chosenMovieId"));
        //TODO Podmienić ticketPriceId ??
        PersonalData personalData = (PersonalData) session.getAttribute("personalData");
        personalDataRepository.save(personalData);
        long personalDataId = personalData.getPersonId();
        Reservation reservation = (Reservation) session.getAttribute("reservation");
        reservation.setPersonalDataId(personalDataId);
        reservationRepository.save(reservation);
        long reservationId = reservation.getReservationId();

        session.invalidate();
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
                    String.valueOf(reservation.getReservationId()),
                    String.valueOf(reservation.getReservationId()),
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

            List<PropertyNotification> propertyNotificationList = new ArrayList<PropertyNotification>() {{
                add(new PropertyNotification("PAYMENT_ID", "12345"));
            }};
            notify(new NotificationResponse(orderResponseNotification, "2016-03-02T12:58:14.828+01:00", propertyNotificationList));
        }
        return new ResponseEntity<>(paymentService.generateOrder(accessToken, reservationId, personalDataId, clientId), HttpStatus.OK);
    }


    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public void sendEmail(long reservationId) throws Exception {
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


}
