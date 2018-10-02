package com.faleknatalia.cinemaBookingSystem.session;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Reservation;

import javax.servlet.http.HttpSession;
import java.util.List;

public class SessionService {

    public static void setChosenMovieId(HttpSession session, Long scheduledMovieId) {
        session.setAttribute("chosenMovieId", scheduledMovieId);
    }

    public static Long getChosenMovieId(HttpSession session) {
        return (Long) session.getAttribute("chosenMovieId");
    }

    public static void setChosenSeatsAndPrices(HttpSession session, List<ChosenSeatAndPrice> chosenSeatAndPrices) {
        session.setAttribute("chosenSeatsAndPrices", chosenSeatAndPrices);
    }

    public static List<ChosenSeatAndPrice> getChosenSeatsAndPrices(HttpSession session) {
        return (List<ChosenSeatAndPrice>) session.getAttribute("chosenSeatsAndPrices");
    }

    public static void setPersonalData(HttpSession session, PersonalData personalData) {
        session.setAttribute("personalData", personalData);
    }

    public static PersonalData getPersonalData(HttpSession session) {
        return (PersonalData) session.getAttribute("personalData");
    }

    public static void setReservation(HttpSession session, Reservation reservation) {
        session.setAttribute("reservation", reservation);
    }

    public static Reservation getReservation(HttpSession session) {
        return (Reservation) session.getAttribute("reservation");
    }

    public static void removeReservation(HttpSession session) {
        session.removeAttribute("reservation");
    }

    public static void removePersonalData(HttpSession session) {
        session.removeAttribute("personalData");
    }

    public static void removeChosenSeatsAndPrice(HttpSession session) {
        session.removeAttribute("chosenSeatsAndPrices");
    }

}
