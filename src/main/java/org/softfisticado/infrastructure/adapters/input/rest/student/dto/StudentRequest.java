package org.softfisticado.infrastructure.adapters.input.rest.student.dto;

import org.softfisticado.domain.model.City;

import java.time.LocalDate;

public class StudentRequest {
    private Integer studentId;
    private String code;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private String gender;
    private String email;
    private Long cityId;
    private String address;
    private String status;

    public StudentRequest(Integer studentId, String code, String name, String lastName, LocalDate birthDate, LocalDate registrationDate, String gender, String email, Long cityId, String address, String status) {
        this.studentId = studentId;
        this.code = code;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.gender = gender;
        this.email = email;
        this.cityId = cityId;
        this.address = address;
        this.status = status;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }
}
