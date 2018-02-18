package com.faleknatalia.cinemaBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class CinemaBookingSystemApplication implements CommandLineRunner {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    CinemaHallRepository cinemaHallRepository;

    public static void main(String[] args) {
        SpringApplication.run(CinemaBookingSystemApplication.class, args);
    }

    public void run(String... strings) throws Exception {

        Movie movie1 = new Movie("The Prestige", "Magnificent", 120);
        Movie movie2 = new Movie("Catch me if you can", "Superb", 125);

        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);

        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            seats.add(new Seat(true, 20));
        }

        CinemaHall cinemaHallOne = new CinemaHall(seats);

        cinemaHallRepository.save(cinemaHallOne);

        movieRepository.save(movie1);
        movieRepository.save(movie2);

        scheduledMovieRepository.save(new ScheduledMovie(now.plusHours(1), cinemaHallOne.getCinemaHallId(), movie1.getMovieId()));
        scheduledMovieRepository.save(new ScheduledMovie(now.plusHours(3).plusMinutes(20), cinemaHallOne.getCinemaHallId(), movie2.getMovieId()));

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
