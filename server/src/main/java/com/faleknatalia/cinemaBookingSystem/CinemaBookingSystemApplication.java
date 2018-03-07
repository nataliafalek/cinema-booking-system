package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.model.CinemaHall;
import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.CinemaHallRepository;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.SeatReservationByScheduledMovieRepository;
import com.faleknatalia.cinemaBookingSystem.service.CinemaHallService;
import com.faleknatalia.cinemaBookingSystem.service.ScheduledMovieService;
import com.faleknatalia.cinemaBookingSystem.service.SeatReservationByScheduledMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@SpringBootApplication
public class CinemaBookingSystemApplication implements CommandLineRunner {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    CinemaHallRepository cinemaHallRepository;

    @Autowired
    CinemaHallService cinemaHallService;

    @Autowired
    ScheduledMovieService scheduledMovieService;

    @Autowired
    SeatReservationByScheduledMovieService seatReservationByScheduledMovieService;

    @Autowired
    SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    public static void main(String[] args) {
        SpringApplication.run(CinemaBookingSystemApplication.class, args);
    }

    public void run(String... strings) throws Exception {

        Movie movie1 = new Movie("The Prestige", "Magnificent", 120);
        Movie movie2 = new Movie("Catch me if you can", "Superb", 125);
        Movie movie3 = new Movie("12 Angry Men", "Splendid", 95);
        Movie movie4 = new Movie("The Silence of the Lambs", "Excellent", 120);
        Movie movie5 = new Movie("Life of Brian", "Funny", 95);
        Movie movie6 = new Movie("Coco", "superb", 120);

        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);


        CinemaHall cinemaHallOne = cinemaHallService.generateCinemaHall(30);
        CinemaHall cinemaHallTwo = cinemaHallService.generateCinemaHall(20);


        cinemaHallRepository.save(cinemaHallOne);
        cinemaHallRepository.save(cinemaHallTwo);


        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);
        movieRepository.save(movie4);
        movieRepository.save(movie5);
        movieRepository.save(movie6);

        Map<LocalDateTime, List<ScheduledMovie>> weekWhatsOn = scheduledMovieService.generateWeekWhatsOn(1, LocalDateTime.now().withMinute(0).withSecond(0).withNano(0));
        weekWhatsOn.values().stream().forEach(s -> scheduledMovieRepository.save(s));

        Map<LocalDateTime, List<ScheduledMovie>> weekWhatsOn2 = scheduledMovieService.generateWeekWhatsOn(2, LocalDateTime.now().withMinute(0).withSecond(0).withNano(0));
        weekWhatsOn2.values().stream().forEach(s -> scheduledMovieRepository.save(s));

        seatReservationByScheduledMovieRepository.save(seatReservationByScheduledMovieService.findSeatsByScheduledMovieId());

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

}
