package com.faleknatalia.cinemaBookingSystem.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity
public class PersonalData {

    private String name;
    private String surname;

    //TODO osobna walidacja dla bazy i frontendu
    @Pattern(regexp = "(^$|[0-9]{9})")
    private String phoneNumber;

    @Email
    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long personId;


    public PersonalData(String name, String surname, String phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public PersonalData() {
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
