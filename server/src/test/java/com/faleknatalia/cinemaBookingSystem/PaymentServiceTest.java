package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.payment.PaymentService;
import com.faleknatalia.cinemaBookingSystem.payment.model.AccessToken;
import com.faleknatalia.cinemaBookingSystem.payment.model.OrderResponse;
import com.faleknatalia.cinemaBookingSystem.repository.ReservationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PaymentServiceTest {

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getRedirectUri() throws Exception {

        //Before
        PersonalData personalData = new PersonalData("Nati", "Falek", "123456789", "nati@gmail.com");
        ArrayList<ChosenSeatAndPrice> listOfSeatsAndPrices = new ArrayList<ChosenSeatAndPrice>() {{
            add(new ChosenSeatAndPrice(20l, 1l));
        }};
        Reservation reservation = new Reservation(2, personalData, listOfSeatsAndPrices);
        reservationRepository.save(reservation);
        AccessToken token = paymentService.generateAccessToken(clientId, clientSecret);

        //when
        OrderResponse response = paymentService.sendOrder(token, reservation.getReservationId(), "322611");
        ResponseEntity<String> result = restTemplate.getForEntity(response.getRedirectUri(), String.class);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}
