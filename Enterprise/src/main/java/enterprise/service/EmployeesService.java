package enterprise.service;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import enterprise.dto.DepartmentDTO;
import enterprise.dto.EmployeeDTO;

public interface EmployeesService
{
    ResponseEntity<?> getEmployees(DepartmentDTO department);
    
    ResponseEntity<?> create(DepartmentDTO department, EmployeeDTO employee, BindingResult bindingResult);
    
    ResponseEntity<?> update(EmployeeDTO employee, BindingResult bindingResult);
    
    ResponseEntity<?> dissmis(EmployeeDTO employee, LocalDateTime when);
    
    ResponseEntity<?> getEmployee(EmployeeDTO employee);
    
    ResponseEntity<?> move(EmployeeDTO employee, DepartmentDTO where);
    
    ResponseEntity<?> moveAll(DepartmentDTO from, DepartmentDTO where);
    
    ResponseEntity<?> getChief(EmployeeDTO employee);
    
    ResponseEntity<?> findBundle(EmployeeDTO qbe);
}
