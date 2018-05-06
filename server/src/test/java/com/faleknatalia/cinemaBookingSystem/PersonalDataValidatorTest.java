package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.validator.PersonalDataValidator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PersonalDataValidatorTest {



    @Test
    public void validateIncorrectPhoneNumber() {
        PersonalData personalData = new PersonalData("Nati", "Mihalek", "12345678339", "aaa@gmail.com");
        assertTrue(new PersonalDataValidator().validate(personalData).isPresent());
    }

    @Test
    public void validateCorrectPhoneNumber() {
        PersonalData personalData = new PersonalData("Nati", "Mihalek", "123456789", "aaa@gmail.com");
        assertFalse(new PersonalDataValidator().validate(personalData).isPresent());
    }

    @Test
    public void validateCorrectEmail() {
        PersonalData personalData = new PersonalData("Nati", "Mihalek", "123456789", "aaa@gmail.com");
        assertFalse(new PersonalDataValidator().validate(personalData).isPresent());
    }

    @Test
    public void validateIncorrectEmail() {
        PersonalData personalData = new PersonalData("Nati", "Mihalek", "123456789", "aaa.gmail.com");
        assertTrue(new PersonalDataValidator().validate(personalData).isPresent());
    }

    @Test
    public void validateCorrectName() {
        PersonalData personalData = new PersonalData("Natił", "Mihałek", "123456789", "aaa@gmail.com");
        assertFalse(new PersonalDataValidator().validate(personalData).isPresent());
    }

    @Test
    public void validateIncorrectName() {
        PersonalData personalData = new PersonalData("NatiLol666", "Mihałek", "123456789", "aaa@gmail.com");
        assertTrue(new PersonalDataValidator().validate(personalData).isPresent());
    }

    @Test
    public void validateIncorrectPersonalData() {
        PersonalData personalData = new PersonalData("NatiLol666", "9Mihalek", "123456789-01", "aaa.gmail.com@");
        assertTrue(new PersonalDataValidator().validate(personalData).isPresent());
    }
}
