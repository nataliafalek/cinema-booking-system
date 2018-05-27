package com.faleknatalia.cinemaBookingSystem.payment;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import com.faleknatalia.cinemaBookingSystem.payment.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.repository.OrderRequestDBRepository;
import com.faleknatalia.cinemaBookingSystem.repository.PersonalDataRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ReservationRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;

import com.faleknatalia.cinemaBookingSystem.repository.TicketPriceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PersonalDataRepository personalDataRepository;

    @Autowired
    SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    @Autowired
    OrderRequestDBRepository orderRequestDBRepository;

    @Autowired
    TicketPriceRepository ticketPriceRepository;

    @Value("${redirect_url}")
    private String redirectUrl;

    @Value("${notify_url}")
    private String notifyUrl;

    @Value("${paymentAuthorizationUrl}")
    private String paymentAuthorizationUrl;

    @Value("${createOrderUrl}")
    private String createOrderUrl;

    @Value("${paymentCustomerIp}")
    private String customerIp;

    public AccessToken generateAccessToken(String client_id, String client_secret) {

        String request = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", client_id, client_secret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(paymentAuthorizationUrl, entity, AccessToken.class);
    }

    public OrderResponse generateOrder(AccessToken token, String reservationId, long personalDataId, String clientId) throws JsonProcessingException {
        Reservation reservation = reservationRepository.findByReservationId(reservationId);
        PersonalData personalData = personalDataRepository.findOne(personalDataId);
        List<TicketPrice> ticketPrices =
                reservation.getChosenSeatsAndPrices().stream().map(chosenSeatAndPrice -> ticketPriceRepository.findOne(chosenSeatAndPrice.getTicketPriceId())).collect(Collectors.toList());

        Buyer buyer = new Buyer(personalData.getEmail(), personalData.getPhoneNumber(), personalData.getName(), personalData.getSurname());

        List<Product> products = new ArrayList<>();
        ticketPrices.stream().map(ticketPrice ->
                products.add(new Product("Ticket", toCents(ticketPrice.getTicketValue()), "1")))
                .collect(Collectors.toList());
        String extOrderId = reservation.getReservationId();
        OrderRequest orderRequest = new OrderRequest(
                extOrderId,
                notifyUrl, customerIp,
                clientId,
                "Bilecik do kina", "PLN", toCents(sumOfTicketPrice(ticketPrices)), buyer, products, redirectUrl);

        ObjectMapper mapper = new ObjectMapper();
        orderRequestDBRepository.save(new OrderRequestsAndResponseDB(extOrderId, "request", mapper.writeValueAsString(orderRequest)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token.getAccess_token());

        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(createOrderUrl, entity, OrderResponse.class);
    }

    private String toCents(int ticketValue) {
        return Integer.toString(ticketValue * 100);
    }

    private int sumOfTicketPrice(List<TicketPrice> chosenSeatsPrice) {
        return chosenSeatsPrice.stream().mapToInt(TicketPrice::getTicketValue).sum();
    }

}
