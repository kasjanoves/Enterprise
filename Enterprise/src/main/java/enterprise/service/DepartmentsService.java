package enterprise.service;

import org.springframework.http.ResponseEntity;

import enterprise.dto.DepartmentDTO;

public interface DepartmentsService
{
    ResponseEntity<?> create(DepartmentDTO department);

    ResponseEntity<?> changeName(DepartmentDTO department, String name);

    ResponseEntity<?> delete(DepartmentDTO department);

    ResponseEntity<?> getInfo(DepartmentDTO department);

    ResponseEntity<?> getSubDepartmentsInfo(DepartmentDTO department);

    ResponseEntity<?> getSubDepartmentsInfoRecursively(DepartmentDTO department);

    ResponseEntity<?> move(DepartmentDTO what, DepartmentDTO were);

    ResponseEntity<?> getParentsInfo(DepartmentDTO department);

    ResponseEntity<?> findByName(String name);

    ResponseEntity<?> getSalaryTotal(DepartmentDTO department);
}
