package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.payment.PaymentService;
import com.faleknatalia.cinemaBookingSystem.payment.model.AccessToken;
import com.faleknatalia.cinemaBookingSystem.payment.model.OrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class SpringTestConfiguration {
    @Bean
    @Primary
    public PaymentService paymentService() throws JsonProcessingException {
        PaymentService paymentService = Mockito.mock(PaymentService.class);
        Mockito.when(
                paymentService.generateAccessToken(Mockito.any(), Mockito.any())
        ).thenReturn(new AccessToken("a", "b", 123, "c"));
        Mockito.when(
                paymentService.sendOrder(Mockito.any(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())
        ).thenReturn(new OrderResponse("1", "2", "AAA", "http://localhost/abcd"));
        return paymentService;
    }
}