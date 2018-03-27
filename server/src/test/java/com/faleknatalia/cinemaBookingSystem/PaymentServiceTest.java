package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;
import com.faleknatalia.cinemaBookingSystem.payment.AccessToken;
import com.faleknatalia.cinemaBookingSystem.payment.OrderResponse;
import com.faleknatalia.cinemaBookingSystem.payment.PaymentService;
import com.faleknatalia.cinemaBookingSystem.repository.PersonalDataRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ReservationRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PersonalDataRepository personalDataRepository;

    @Test
    public void getAccessToken() throws Exception {

        //Before
        PersonalData personalData = new PersonalData("Nati", "Falek", "123456789", "nati@gmail.com");
        personalDataRepository.save(personalData);
        ArrayList<Long> listOfSeats = new ArrayList<Long>() {{
            add(20l);
        }};
        Reservation reservation = new Reservation(2, personalData.getPersonId(), listOfSeats);
        reservationRepository.save(reservation);

        AccessToken token = paymentService.generateAccessToken("322611", "7bf401d342210d73b85081c0a2fae474");

        System.out.println(token.getAccess_token());

        OrderResponse response = paymentService.generateOrder(token, reservation.getReservationId(), "322611");

        System.out.println(response.getRedirectUri());
    }

}
