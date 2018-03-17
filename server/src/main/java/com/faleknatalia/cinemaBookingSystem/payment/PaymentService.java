package com.faleknatalia.cinemaBookingSystem.payment;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.PersonalDataRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ReservationRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PersonalDataRepository personalDataRepository;

    @Autowired
    SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    public AccessToken generateAccessToken(String client_id, String client_secret) {
        String url = "https://secure.snd.payu.com/pl/standard/user/oauth/authorize";

        RestTemplate restTemplate = new RestTemplate();

        String requestJson = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", client_id, client_secret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        return restTemplate.postForObject(url, entity, AccessToken.class);
    }

    public OrderResponse generateOrder(AccessToken token, long reservationId, String clientId) {

        //Order data
        Reservation reservation = reservationRepository.findOne(reservationId);
        PersonalData personalData = personalDataRepository.findOne(reservation.getPersonalDataId());
        List<SeatReservationByScheduledMovie> chosenSeatsPrice = seatReservationByScheduledMovieRepository.findBySeatIdInAndScheduledMovieId(reservation.getChosenSeatId(), reservation.getChosenMovieId());

        String ticketPriceInCents = String.valueOf(sumOfTicketPrice(chosenSeatsPrice) * 100);
        String url = "https://secure.snd.payu.com/api/v2_1/orders";
        RestTemplate restTemplate = new RestTemplate();
        Buyer buyer = new Buyer(personalData.getEmail(), personalData.getPhoneNumber(), personalData.getName(), personalData.getSurname());
        Product product = new Product("Ticket", ticketPriceInCents, "1");
        List<Product> products = new ArrayList<>();
        products.add(product);
        OrderRequest orderRequest = new OrderRequest(Long.toString(reservationId), "http://localhost:8080/notify", "127.0.0.1", clientId, "Bilecik do kina", "PLN", ticketPriceInCents, buyer, products, "http://localhost:3000/#/paymentSuccess");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token.getAccess_token());

        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, headers);


        return restTemplate.postForObject(url, entity, OrderResponse.class);

    }

    private int sumOfTicketPrice(List<SeatReservationByScheduledMovie> chosenSeatsPrice) {
        int sum = 0;
        for (SeatReservationByScheduledMovie s : chosenSeatsPrice) {
            sum = sum + s.getTicketPrice();
        }
        return sum;
    }


}
