package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.model.CinemaHall;
import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import com.faleknatalia.cinemaBookingSystem.repository.*;
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
    TicketPriceRepository ticketPriceRepository;

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

        Movie movie1 = new Movie("The Prestige", "Magnificent", 120, "http://1.fwcdn.pl/po/99/45/259945/7536864.6.jpg");
        Movie movie2 = new Movie("Catch me if you can", "Superb", 125, "http://1.fwcdn.pl/po/23/62/32362/7519166.6.jpg");
        Movie movie3 = new Movie("12 Angry Men", "Splendid", 95, "http://1.fwcdn.pl/po/07/01/30701/7492190.6.jpg");
        Movie movie4 = new Movie("The Silence of the Lambs", "Excellent", 120, "http://1.fwcdn.pl/po/10/47/1047/7714177.6.jpg");
        Movie movie5 = new Movie("Life of Brian", "Funny", 95, "http://1.fwcdn.pl/po/10/99/1099/7364767.6.jpg");
        Movie movie6 = new Movie("Coco", "superb", 120, "http://1.fwcdn.pl/po/22/60/752260/7813486.6.jpg");

        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);


        CinemaHall cinemaHallOne = new CinemaHallService().generateCinemaHall(5,6);
        CinemaHall cinemaHallTwo = new CinemaHallService().generateCinemaHall(5,5);


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

        TicketPrice ticketPriceForStudent = new TicketPrice("student",7);
        TicketPrice ticketPriceNormal = new TicketPrice("normal",10);
        TicketPrice ticketPriceForSenior = new TicketPrice("senior",8);
        ticketPriceRepository.save(ticketPriceNormal);
        ticketPriceRepository.save(ticketPriceForSenior);
        ticketPriceRepository.save(ticketPriceForStudent);

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
