package com.faleknatalia.cinemaBookingSystem.validator;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalDataValidator {

    public static Optional<String> validate(PersonalData personalData) {
        Pattern validEmailAddressRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Pattern validPhoneNumberRegex = Pattern.compile("\\d{9}|(?:\\d{3}-){2}\\d{3}");
        Pattern validNameRegex = Pattern.compile("\\p{L}+");
        Matcher matcherPhoneNumber = validPhoneNumberRegex.matcher(personalData.getPhoneNumber());
        Matcher matcherEmailAddress = validEmailAddressRegex.matcher(personalData.getEmail());
        Matcher matcherName = validNameRegex.matcher(personalData.getName());
        Matcher matcherSurname = validNameRegex.matcher(personalData.getSurname());
        if (matcherPhoneNumber.matches() && matcherEmailAddress.matches()
                && matcherName.matches() && matcherSurname.matches()) {
            return Optional.empty();
        } else {
            return Optional.of("Bad data format");
        }

    }

}
