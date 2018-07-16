package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.constants.Constants;
import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.mail.EmailSender;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.payment.NotificationResponseDB;
import com.faleknatalia.cinemaBookingSystem.payment.PaymentService;
import com.faleknatalia.cinemaBookingSystem.payment.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.repository.NotificationResponseDBRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ReservationRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketDataService;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketGeneratorPdf;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

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

    @Autowired
    private Constants constants;

    @Transactional
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> saveReservationAndRedirectToPayment(HttpServletResponse response, HttpSession session) throws Exception {
        Reservation reservation = saveReservation(session);
        AccessToken accessToken = paymentService.generateAccessToken(constants.getClientId(), constants.getClientSecret());
        if (constants.isDevMode()) {
            invokeNotifyAsPayu(reservation);
        }
        OrderResponse orderResponse = paymentService.sendOrder(accessToken, reservation.getReservationId(), constants.getClientId());
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public void sendEmail(String extOrderId) throws Exception {
        ByteArrayOutputStream doc = new TicketGeneratorPdf().generateTicket(ticketDataService.findMovie(extOrderId));
        String email = reservationRepository.findByReservationId(extOrderId).getPersonalData().getEmail();
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
                constants.getNotifyUrl(),
                "127.0.0.1",
                constants.getClientId(),
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

    private Reservation saveReservation(HttpSession session) {
        Reservation reservation = (Reservation) session.getAttribute("reservation");
        List<ChosenSeatAndPrice> chosenSeatAndPrices = (List<ChosenSeatAndPrice>) session.getAttribute("chosenSeatsAndPrices");
        List<Long> chosenSeatsIds = chosenSeatAndPrices.stream().map(chosenSeatAndPrice -> chosenSeatAndPrice.getSeatId()).collect(Collectors.toList());

        long chosenMovieId = (long) session.getAttribute("chosenMovieId");
        seatReservationByScheduledMovieRepository.reserveSeat(chosenSeatsIds, chosenMovieId);
        reservationRepository.save(reservation);
        session.invalidate();
        return reservation;
    }

}
