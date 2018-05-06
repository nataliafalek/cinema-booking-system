package com.faleknatalia.cinemaBookingSystem.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class PersonalData implements Serializable {

    private String name;
    private String surname;

    private String phoneNumber;

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

    public long getPersonId() {
        return personId;
    }
}
