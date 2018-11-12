package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.constants.Constants;
import com.faleknatalia.cinemaBookingSystem.controllers.MakeReservationController;
import com.faleknatalia.cinemaBookingSystem.controllers.PaymentController;
import com.faleknatalia.cinemaBookingSystem.controllers.SessionController;
import com.faleknatalia.cinemaBookingSystem.controllers.WhatsOnController;
import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.dto.ReservationSummaryDto;
import com.faleknatalia.cinemaBookingSystem.dto.ScheduledMovieDetailsDto;
import com.faleknatalia.cinemaBookingSystem.dto.SessionParameters;
import com.faleknatalia.cinemaBookingSystem.mail.EmailSender;
import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.payment.PaymentService;
import com.faleknatalia.cinemaBookingSystem.payment.model.AccessToken;
import com.faleknatalia.cinemaBookingSystem.payment.model.OrderResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MakeReservationControllerTest {

    @MockBean
    private EmailSender emailSender;

    @Autowired
    private WhatsOnController whatsOnController;

    @Autowired
    private MakeReservationController makeReservationController;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    PaymentService paymentService;

    @Autowired
    private PaymentController paymentController;

    @Autowired
    private SessionController sessionController;

    @Autowired
    private Constants constants;

    @Test
    public void reserveSeats() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        ResponseEntity<List<ScheduledMovieDetailsDto>> listResponseEntity = whatsOnController.whatsOn();
        ScheduledMovieDetailsDto scheduledMovieDetailsDto = listResponseEntity.getBody().get(0);
        long scheduledMovieId = scheduledMovieDetailsDto.getScheduledMovieId();
        ResponseEntity<List<List<SeatReservationByScheduledMovie>>> seats = makeReservationController.seatsByCinemaHallAndMovie(mockHttpSession, scheduledMovieId);
        SeatReservationByScheduledMovie chosenSeatOne = seats.getBody().get(0).get(0);
        SeatReservationByScheduledMovie chosenSeatTwo = seats.getBody().get(0).get(1);
        List<ChosenSeatAndPrice> chosenSeatAndPrices = Arrays.asList(
                new ChosenSeatAndPrice(chosenSeatOne.getSeat().getSeatId(), chosenSeatOne.getTicketPriceId()),
                new ChosenSeatAndPrice(chosenSeatTwo.getSeat().getSeatId(), chosenSeatTwo.getTicketPriceId())
        );
        ResponseEntity<List<SeatReservationByScheduledMovie>> chosenSeatsAndPrices = makeReservationController.chosenSeat(mockHttpSession, scheduledMovieId, chosenSeatAndPrices);
        PersonalData personalData = new PersonalData("Nati", "Falek", "123456789", "faleknatalia@gmail.com");
        makeReservationController.createReservation(mockHttpSession, personalData);
        ResponseEntity<ReservationSummaryDto> reservationSummaryDtoResponseEntity = makeReservationController.reservationSummary(mockHttpSession);
        ReservationSummaryDto reservationSummaryDto = reservationSummaryDtoResponseEntity.getBody();
        List<ChosenSeatAndPrice> chosenSeatAndPricesSummary = reservationSummaryDto.getTicketData().getSeatAndPriceDetails().stream().map(
                seatAndPriceDetails -> new ChosenSeatAndPrice(seatAndPriceDetails.getSeat().getSeatId(), seatAndPriceDetails.getTicketPrice().getTicketPriceId()))
                .collect(Collectors.toList());

        assertEquals(reservationSummaryDto.getTicketData().getMovieTitle(), scheduledMovieDetailsDto.getMovieTitle());
        assertEquals(reservationSummaryDto.getTicketData().getProjectionDate(), scheduledMovieDetailsDto.getDateOfProjection().toLocalDate().toString());
        assertThat(chosenSeatAndPrices.equals(chosenSeatAndPricesSummary));
        assertEquals(reservationSummaryDto.getPersonalData().getEmail(), personalData.getEmail());

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        SessionParameters sessionParameters = sessionController.getSessionParameters(mockHttpSession);
        assertEquals(sessionParameters.getChosenMovieId(), scheduledMovieId);

        AccessToken accessToken = new AccessToken("8f79c971-195e-43f5-bd83-ad2104414acc", "bearer", 43199, "client_credentials");
        OrderResponse orderResponse = new OrderResponse("WZHF5FFDRJ140731GUEST000P01", "12345", "SUCCESS", constants.getRedirectUrl());
        Mockito.when(paymentService.generateAccessToken(constants.getClientId(), constants.getClientSecret())).thenReturn(accessToken);
        Mockito.when(paymentService.sendOrder(Mockito.eq(accessToken), Mockito.anyString(), Mockito.eq(constants.getClientId()))).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> orderResponseResponseEntity = paymentController.saveReservationAndRedirectToPayment(mockHttpServletResponse, mockHttpSession);
        assertEquals("http://localhost:3000/#/paymentSuccess", orderResponseResponseEntity.getBody().getRedirectUri());
    }
}
