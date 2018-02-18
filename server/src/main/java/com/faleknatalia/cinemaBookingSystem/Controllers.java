package com.faleknatalia.cinemaBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Controllers {

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

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
                                    sm.getDateOfProjection()
                            );
                        }).collect(Collectors.toList());

        return new ResponseEntity<>(scheduledMovieDetails, HttpStatus.OK);
    }

}
