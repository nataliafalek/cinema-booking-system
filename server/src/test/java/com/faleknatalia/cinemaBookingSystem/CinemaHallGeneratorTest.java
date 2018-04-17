package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.model.CinemaHall;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.util.CinemaHallGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CinemaHallGeneratorTest {


    @Test
    public void generateSeats() {
        //given
        List<Seat> seats = new ArrayList<Seat>() {{
            add(new Seat(1,1,1));
            add(new Seat(2,1,2));
            add(new Seat(1,2,1));
            add(new Seat(2,2,2));
            add(new Seat(1,3,1));
            add(new Seat(2,3,2));
        }};
        CinemaHall cinemaHall = new CinemaHall(seats);

        //when
        CinemaHall cinemaHallAuto = new CinemaHallGenerator().generateCinemaHall(3,2);



        System.out.println(Arrays.toString(cinemaHallAuto.getSeats().toArray()));
        System.out.println(Arrays.toString(cinemaHall.getSeats().toArray()));


        //then
        //assertEquals(equalLists,TRUE);
    }

    @Test
    public void deleteSeatsFromCinemaHall() {
        // given
        List<Seat> seats = new ArrayList<Seat>() {{
            add(new Seat(1,1,1));
            add(new Seat(2,1,2));
            add(new Seat(3,1,3));
            add(new Seat(1,2,1));
            add(new Seat(2,2,2));
            add(new Seat(3,2,3));
        }};
        CinemaHall cinemaHallAuto = new CinemaHall(seats);
        System.out.println(Arrays.toString(cinemaHallAuto.getSeats().toArray()));

        //then
        cinemaHallAuto.getSeats().subList(0,2).clear();
            cinemaHallAuto.getSeats().stream()
                .filter(seat -> seat.getRowNumber()==1)
                .forEach(seatFromOneRow -> {
                    int nr = seatFromOneRow.getSeatNumber();
                    seatFromOneRow.setSeatNumber(nr -2);
                });

        System.out.println(Arrays.toString(cinemaHallAuto.getSeats().toArray()));

    }
}