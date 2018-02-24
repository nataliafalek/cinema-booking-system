package com.faleknatalia.cinemaBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

//TODO wydzielic do serwisow logike z controllerow

@RestController
public class Controllers {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Controllers.class);


    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private PersonalDataRepository personalDataRepository;

    @Autowired
    private SeatRepository seatRepository;

    //TODO optymalizacja - wydzielic metode do serwisu osobnego, tak by efektywnie laczyc ScheduledMovie i ScheduledMovieDetails
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

    @Transactional
    @RequestMapping(value = "/cinemaHall/seats/choose/{seatId}", method = RequestMethod.GET)
    public ResponseEntity<Seat> chosenSeat(@PathVariable long seatId) {
        seatRepository.setFalseForChosenSeat(seatId);
        Seat chosenSeat = seatRepository.findOne(seatId);
        return new ResponseEntity<>(chosenSeat, HttpStatus.OK);
    }

    @RequestMapping(value = "/cinemaHall/addPerson", method = RequestMethod.POST)
    public ResponseEntity<Void> addPerson(@RequestBody PersonalData personalData) {
        personalDataRepository.save(personalData);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
