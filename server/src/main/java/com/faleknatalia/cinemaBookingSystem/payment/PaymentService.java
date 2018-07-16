package com.faleknatalia.cinemaBookingSystem.payment;

import com.faleknatalia.cinemaBookingSystem.constants.Constants;
import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import com.faleknatalia.cinemaBookingSystem.payment.model.*;
import com.faleknatalia.cinemaBookingSystem.payment.repository.OrderRequestDBRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ReservationRepository;
import com.faleknatalia.cinemaBookingSystem.repository.TicketPriceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentService {

    private static final String QUANTITY = "1";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private OrderRequestDBRepository orderRequestDBRepository;

    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Constants constants;

    public AccessToken generateAccessToken(String client_id, String client_secret) {

        String request = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", client_id, client_secret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(constants.getPaymentAuthorizationUrl(), entity, AccessToken.class);
    }

    public OrderResponse sendOrder(AccessToken token, String reservationId, String clientId) throws JsonProcessingException {
        OrderRequest orderRequest = generateOrderRequest(reservationId, clientId);
        ObjectMapper mapper = new ObjectMapper();
        orderRequestDBRepository.save(new OrderRequestsAndResponseDB(orderRequest.getExtOrderId(), "request", mapper.writeValueAsString(orderRequest)));
        return httpPostPayuOrder(token, orderRequest);
    }


    private String toCents(int ticketValue) {
        return Integer.toString(ticketValue * 100);
    }

    private int sumOfTicketPrice(List<TicketPrice> chosenSeatsPrice) {
        return chosenSeatsPrice.stream().mapToInt(TicketPrice::getTicketValue).sum();
    }

    private OrderResponse httpPostPayuOrder(AccessToken token, OrderRequest orderRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token.getAccess_token());

        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, headers);

        return restTemplate.postForObject(constants.getCreateOrderUrl(), entity, OrderResponse.class);
    }

    private OrderRequest generateOrderRequest(String reservationId, String clientId) {
        Reservation reservation = reservationRepository.findByReservationId(reservationId);
        PersonalData personalData = reservation.getPersonalData();
        List<TicketPrice> ticketPrices =
                reservation.getChosenSeatsAndPrices().stream().map(chosenSeatAndPrice -> ticketPriceRepository.findOne(chosenSeatAndPrice.getTicketPriceId())).collect(Collectors.toList());

        Buyer buyer = new Buyer(personalData.getEmail(), personalData.getPhoneNumber(), personalData.getName(), personalData.getSurname());

        List<Product> products = ticketPrices.stream().map(ticketPrice ->
                new Product("Ticket", toCents(ticketPrice.getTicketValue()), QUANTITY))
                .collect(Collectors.toList());
        String extOrderId = reservation.getReservationId();
        return new OrderRequest(
                extOrderId,
                constants.getNotifyUrl(), constants.getCustomerIp(),
                clientId,
                "Bilecik do kina", "PLN", toCents(sumOfTicketPrice(ticketPrices)), buyer, products, constants.getRedirectUrl());

    }

}
