package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.mail.EmailSender;
import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.*;
import com.faleknatalia.cinemaBookingSystem.payment.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.repository.NotificationResponseDBRepository;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketGeneratorPdf;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);


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
    private EmailSender emailSender;

    @Autowired
    private NotificationResponseDBRepository notificationResponseDBRepository;


    @Value("${dev_mode}")
    private boolean devMode;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${notify_url}")
    private String notifyUrl;

    @Transactional
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> saveReservationAndRedirectToPayment(HttpServletResponse response, HttpSession session) throws Exception {
        PersonalData personalData = (PersonalData) session.getAttribute("personalData");
        Reservation reservation = (Reservation) session.getAttribute("reservation");
        List<ChosenSeatAndPrice> chosenSeatAndPrices = (List<ChosenSeatAndPrice>) session.getAttribute("chosenSeatsAndPrices");
        List<Long> chosenSeatsIds = chosenSeatAndPrices.stream().map(chosenSeatAndPrice -> chosenSeatAndPrice.getSeatId()).collect(Collectors.toList());

        AccessToken accessToken = paymentService.generateAccessToken(clientId, clientSecret);

        seatReservationByScheduledMovieRepository.reserveSeat(chosenSeatsIds, (long) session.getAttribute("chosenMovieId"));
        personalDataRepository.save(personalData);
        long personalDataId = personalData.getPersonId();
        reservation.setPersonalDataId(personalDataId);
        reservationRepository.save(reservation);
        String reservationId = reservation.getReservationId();

        if (devMode) {
            invokeNotifyAsPayu(reservation);
        }
        OrderResponse orderResponse = paymentService.sendOrder(accessToken, reservationId, personalDataId, clientId);
        session.invalidate();
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public void sendEmail(String extOrderId) throws Exception {
        ByteArrayOutputStream doc = new TicketGeneratorPdf().generateTicket(ticketDataService.findMovie(extOrderId));
        long personalDataId = reservationRepository.findByReservationId(extOrderId).getPersonalDataId();
        String email = personalDataRepository.findOne(personalDataId).getEmail();
        emailSender.sendEmail(email, "NatiCinema cinema ticket", "Please take this ticket and show before projection of the movie.", doc);
    }

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void notify(@RequestBody String notificationResponse) throws Exception {
        JSONObject jsonObject = new JSONObject(notificationResponse);
        JSONObject order = jsonObject.getJSONObject("order");
        String extOrderId = order.getString("extOrderId");
        notificationResponseDBRepository.save(new NotificationResponseDB(extOrderId, notificationResponse));
        if (order.getString("status").equals("COMPLETED")) {
            sendEmail(extOrderId);
        }

        logger.info("PayU notification response: \n" + notificationResponse);
    }

    private void invokeNotifyAsPayu(Reservation reservation) throws Exception {
        NotificationResponse notificationResponse = testNotificationResponse(reservation);
        ObjectMapper mapper = new ObjectMapper();
        notify(mapper.writeValueAsString(notificationResponse));
    }

    private NotificationResponse testNotificationResponse(Reservation reservation) {
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
                notifyUrl,
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

        return new NotificationResponse(orderResponseNotification, "2016-03-02T12:58:14.828+01:00", propertyNotificationList);
    }

}
