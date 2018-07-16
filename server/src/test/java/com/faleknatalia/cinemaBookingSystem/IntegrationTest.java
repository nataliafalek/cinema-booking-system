package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.controllers.MakeReservationController;
import com.faleknatalia.cinemaBookingSystem.controllers.PaymentController;
import com.faleknatalia.cinemaBookingSystem.controllers.WhatsOnController;
import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.dto.ReservationSummaryDto;
import com.faleknatalia.cinemaBookingSystem.dto.ScheduledMovieDetailsDto;
import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.payment.model.OrderRequest;
import com.faleknatalia.cinemaBookingSystem.payment.model.OrderResponse;
import com.faleknatalia.cinemaBookingSystem.ticket.SeatAndPriceDetails;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTest {
    @Autowired
    private MakeReservationController makeReservationController;

    @Autowired
    private WhatsOnController whatsOnController;

    @Autowired
    private PaymentController paymentController;


    @Test
    public void integrationTest() throws Exception {
        //when
        ResponseEntity<List<ScheduledMovieDetailsDto>> whatsOnResponseEntity = whatsOnController.whatsOn();

        //then
        List<ScheduledMovieDetailsDto> listResponse = whatsOnResponseEntity.getBody();
        Assert.assertEquals(200, whatsOnResponseEntity.getStatusCode().value());

        //given
        ScheduledMovieDetailsDto scheduledMovieDetailsDto = listResponse.get(0);
        long scheduledMovieId = scheduledMovieDetailsDto.getScheduledMovieId();

        //when
        ResponseEntity<List<List<SeatReservationByScheduledMovie>>> seatsByScheduledMovieMatrixResponseEntity = makeReservationController.seatsByCinemaHallAndMovie(scheduledMovieId);

        //then
        List<List<SeatReservationByScheduledMovie>> seatsByScheduledMovieMatrix = seatsByScheduledMovieMatrixResponseEntity.getBody();
        Assert.assertEquals(200, seatsByScheduledMovieMatrixResponseEntity.getStatusCode().value());

        //given
        SeatReservationByScheduledMovie seatReservationByScheduledMovie = seatsByScheduledMovieMatrix.get(0).get(0);
        long seatId = seatReservationByScheduledMovie.getSeat().getSeatId();
        long ticketPriceId = seatReservationByScheduledMovie.getTicketPriceId();
        List<ChosenSeatAndPrice> chosenSeatAndPrices = Arrays.asList(new ChosenSeatAndPrice(seatId, ticketPriceId));
        MockHttpSession session = new MockHttpSession();

        //when
        ResponseEntity<List<SeatReservationByScheduledMovie>> seatReservationEntity = makeReservationController.chosenSeat(session, scheduledMovieId, chosenSeatAndPrices);

        //then
        //SeatReservationByScheduledMovie seatReservation = seatReservationEntity.getBody().get(0);
        Assert.assertEquals(200, seatReservationEntity.getStatusCode().value());
        Assert.assertEquals(scheduledMovieId, session.getAttribute("chosenMovieId"));
        Assert.assertEquals(chosenSeatAndPrices, session.getAttribute("chosenSeatsAndPrices"));

        //given
        PersonalData personalData = new PersonalData("Nati", "Nat", "123456789", "naticinema@gmail.com");

        //when
        ResponseEntity<String> addPersonalDataResponseEntity = makeReservationController.createReservation(session, personalData);

        //then
       // String reservationId = addPersonalDataResponseEntity.getBody();
        Assert.assertEquals(personalData, session.getAttribute("personalData"));
        Assert.assertEquals(200, addPersonalDataResponseEntity.getStatusCode().value());

        //when
        ResponseEntity<ReservationSummaryDto> reservationSummaryDtoResponseEntity = makeReservationController.reservationSummary(session);

        //then
        ReservationSummaryDto reservationSummaryDto = reservationSummaryDtoResponseEntity.getBody();
        Assert.assertEquals(200, reservationSummaryDtoResponseEntity.getStatusCode().value());
        SeatAndPriceDetails seatAndPriceDetails = reservationSummaryDto.getTicketData().getSeatAndPriceDetails().get(0);
        Assert.assertEquals(seatId, seatAndPriceDetails.getSeat().getSeatId());
        Assert.assertEquals(ticketPriceId, seatAndPriceDetails.getTicketPrice().getTicketPriceId());
        Assert.assertEquals(personalData.getName(), reservationSummaryDto.getPersonalData().getName());

        //PAYMENT
        //given
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        //when
        ResponseEntity<OrderResponse> orderResponseResponseEntity = paymentController.saveReservationAndRedirectToPayment(httpServletResponse, session);

        //then
        OrderResponse orderResponse = orderResponseResponseEntity.getBody();
        Assert.assertEquals(200, orderResponseResponseEntity.getStatusCode().value());


    }


}