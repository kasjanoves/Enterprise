package enterprise.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import enterprise.dto.DepartmentDTO;

public interface DepartmentsService
{
    ResponseEntity<?> create(DepartmentDTO department, Errors errors);

    ResponseEntity<?> rename(DepartmentDTO department, String name);

    ResponseEntity<?> delete(DepartmentDTO department);

    ResponseEntity<?> get(DepartmentDTO department);

    ResponseEntity<?> getSubDepartments(DepartmentDTO department);

    ResponseEntity<?> getSubDepartmentsRecursively(DepartmentDTO department);

    ResponseEntity<?> move(DepartmentDTO what, DepartmentDTO were);

    ResponseEntity<?> getParents(DepartmentDTO department);

    ResponseEntity<?> findByName(String name);

    ResponseEntity<?> getSalaryTotal(DepartmentDTO department);

}
