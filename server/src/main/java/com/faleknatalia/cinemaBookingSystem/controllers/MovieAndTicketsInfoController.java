package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.TicketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieAndTicketsInfoController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<>(movieRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/ticketPriceList", method = RequestMethod.GET)
    public ResponseEntity<List<TicketPrice>> ticketPriceList() {
        return new ResponseEntity<>(ticketPriceRepository.findAll(), HttpStatus.OK);
    }
}
