package com.faleknatalia.cinemaBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Controllers {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Controllers.class);


    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;


    @RequestMapping(value = "/whatsOn", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovieDetails>> whatsOn() {

        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAll();
        List<ScheduledMovieDetails> scheduledMovieDetails =
                scheduledMovies.stream().map(
                        sm -> {
                            Movie one = movieRepository.findOne(sm.getMovieId());
                            return new ScheduledMovieDetails(
                                    one.getTitle(),
                                    one.getDurationInMinutes(),
                                    sm.getDateOfProjection(),
                                    sm.getScheduledMovieId()
                            );
                        }).collect(Collectors.toList());

        return new ResponseEntity<>(scheduledMovieDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/seats/{scheduledMovieId}", method = RequestMethod.GET)
    public ResponseEntity<CinemaHall> cinemaHallSeatsState(@PathVariable long scheduledMovieId) {
        long cinemaHallId = scheduledMovieRepository.findOne(scheduledMovieId).getCinemaHallId();
        return new ResponseEntity<>(cinemaHallRepository.findOne(cinemaHallId), HttpStatus.OK);
    }


}
