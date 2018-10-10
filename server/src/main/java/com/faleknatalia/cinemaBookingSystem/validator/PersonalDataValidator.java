package com.faleknatalia.cinemaBookingSystem.validator;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalDataValidator {

    public static Optional<String> validate(PersonalData personalData) {
        Pattern validEmailAddressRegex = Pattern.compile("[^@]+@[^@]+\\.[a-zA-Z]{2,}", Pattern.CASE_INSENSITIVE);
        Pattern validPhoneNumberRegex = Pattern.compile("(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)");
        Pattern validNameRegex = Pattern.compile("(\\p{L}+)\\s?(\\p{L}+)?");
        Pattern validSurnameNameRegex = Pattern.compile("(\\p{L}+)(-|\\s)?(\\p{L}+)?");
        Matcher matcherPhoneNumber = validPhoneNumberRegex.matcher(personalData.getPhoneNumber());
        Matcher matcherEmailAddress = validEmailAddressRegex.matcher(personalData.getEmail());
        Matcher matcherName = validNameRegex.matcher(personalData.getName());
        Matcher matcherSurname = validSurnameNameRegex.matcher(personalData.getSurname());
        if (matcherPhoneNumber.matches() && matcherEmailAddress.matches()
                && matcherName.matches() && matcherSurname.matches()) {
            return Optional.empty();
        } else {
            return Optional.of("Bad data format");
        }

    }

}
