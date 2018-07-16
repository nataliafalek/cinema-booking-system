package com.faleknatalia.cinemaBookingSystem.data;

import com.faleknatalia.cinemaBookingSystem.model.CinemaHall;
import com.faleknatalia.cinemaBookingSystem.model.Seat;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class CinemaHallGenerator {

    public CinemaHall generateCinemaHall(int rows, int columns, int howManySeatsRemove) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seats.add(new Seat(j, i, j));
            }
        }
        List<Seat> removedSeats = new ArrayList<>();


        for (int i = 1; i <= howManySeatsRemove; i++) {
            int randomNumber = ThreadLocalRandom.current().nextInt(1, seats.size() - 1);
            seats.remove(randomNumber);
            removedSeats.add(seats.get(randomNumber));
        }

        Map<Integer, List<Seat>> removedSeatsMap = removedSeats.stream().collect(Collectors.groupingBy(seat -> seat.getRowNumber()));


        seats.stream()
                .filter(seat ->
                        removedSeatsMap.containsKey(seat.getRowNumber())
                )
                .forEach(seatFromOneRow -> {
                    removedSeatsMap.forEach((k, v) -> {
                        if (seatFromOneRow.getRowNumber() == k) {
                            v.stream().forEach(value -> {
                                if (seatFromOneRow.getSeatNumber() >= value.getSeatNumber()) {
                                    seatFromOneRow.setSeatNumber(seatFromOneRow.getSeatNumber() - 1);
                                }
                            });
                        }
                    });
                });

        return new CinemaHall(seats);
    }

}
