package org.softfisticado.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Student {
    private Long id;
    private Integer studentId;
    private String code;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private String gender;
    private String email;
    private City city;
    private String address;
    private String status;
    private LocalDate dateCreated;
    private LocalDate dateModified;

    public Student() {
    }

    public Student(Integer studentId, String code, String name, String lastName, LocalDate birthDate, LocalDate registrationDate, String gender, String email, City city, String address, String status, LocalDate dateCreated, LocalDate dateModified) {
        this.studentId = studentId;
        this.code = code;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.gender = gender;
        this.email = email;
        this.city = city;
        this.address = address;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public Student(Integer studentId, String code, String name, String lastName, LocalDate birthDate, LocalDate registrationDate, String gender, String email, City city, String address, String status) {
        this.studentId = studentId;
        this.code = code;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.gender = gender;
        this.email = email;
        this.city = city;
        this.address = address;
        this.status = status;
    }

    public City getCity() {
        return city;
    }
}
