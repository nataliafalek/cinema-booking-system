package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class CinemaHall {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seat> seats;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cinemaHallId;

    public CinemaHall(List<Seat> seats) {
        this.seats = seats;
    }

    public CinemaHall() {
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public long getCinemaHallId() {
        return cinemaHallId;
    }
}
