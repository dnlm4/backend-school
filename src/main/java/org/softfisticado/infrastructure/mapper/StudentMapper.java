package org.softfisticado.infrastructure.mapper;

import org.softfisticado.domain.model.City;
import org.softfisticado.domain.model.Student;
import org.softfisticado.infrastructure.adapters.input.rest.student.dto.StudentRequest;
import org.softfisticado.infrastructure.persistence.entity.CityEntity;
import org.softfisticado.infrastructure.persistence.entity.StudentEntity;

public class StudentMapper {
    public static Student toStudent(StudentRequest request) {
        return new Student(
                request.getStudentId(),
                request.getCode(),
                request.getName(),
                request.getLastName(),
                request.getBirthDate(),
                request.getRegistrationDate(),
                request.getGender(),
                request.getEmail(),
                new City(request.getCityId()),
                request.getAddress(),
                request.getStatus()
        );
    }
    public static StudentEntity toStudentEntity(Student student) {
        return new StudentEntity(
                student.getStudentId(),
                student.getCode(),
                student.getName(),
                student.getLastName(),
                student.getBirthDate(),
                student.getRegistrationDate(),
                student.getGender(),
                student.getEmail(),
                new CityEntity(student.getCity().getId()),
                student.getAddress(),
                student.getStatus()
        );
    }
}
