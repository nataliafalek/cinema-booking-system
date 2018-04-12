package com.faleknatalia.cinemaBookingSystem.service;

import com.faleknatalia.cinemaBookingSystem.model.CinemaHall;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class CinemaHallService {

    public CinemaHall generateCinemaHall(int rows,int columns) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seats.add(new Seat(j, i, j));
            }
        }
        //remove 2 elements from first row
        seats.remove(0);
        seats.remove(0);
        seats.stream()
                .filter(seat -> seat.getRowNumber()==1)
                .forEach(seatFromOneRow -> {
                    int nr = seatFromOneRow.getSeatNumber();
                    seatFromOneRow.setSeatNumber(nr - 2);
                });

        return new CinemaHall(seats);
    }

}
