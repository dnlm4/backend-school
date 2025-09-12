package org.softfisticado.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@Table(name = "student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_id",unique = true)
    private Integer studentId;
    @Column(name = "code",unique = true, nullable = false)
    private String code;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    private String gender;
    private String email;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;
    private String address;
    private String status;
    @Column(name = "date_created")
    private LocalDate dateCreated;
    @Column(name = "date_modified")
    private LocalDate dateModified;

    public StudentEntity(Integer studentId, String code, String name, String lastName, LocalDate birthDate, LocalDate registrationDate, String gender, String email, CityEntity city, String address, String status) {
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

    public StudentEntity(Integer studentId, String code, String name, String lastName, LocalDate birthDate, LocalDate registrationDate, String gender, String email, CityEntity city, String address, String status, LocalDate dateCreated, LocalDate dateModified) {
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

}
