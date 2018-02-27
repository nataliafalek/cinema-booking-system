package com.faleknatalia.cinemaBookingSystem.service;

import com.faleknatalia.cinemaBookingSystem.model.CinemaHall;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CinemaHallService {

    @Autowired
    SeatRepository seatRepository;

    public CinemaHall generateCinemaHall(int howManySeats, int ticketPrice) {
        List<Seat> seats = new ArrayList<>();
        long lastSeatId = seatRepository.countAllBySeatIdIsNotNull();
        for (int i = (int) lastSeatId; i < howManySeats; i++) {
            seats.add(new Seat(i - (int) lastSeatId + 1, true, ticketPrice));
        }
        return new CinemaHall(seats);
    }
}
