package enterprise.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import enterprise.domain.Department;
import enterprise.domain.Employee;
import enterprise.domain.EmployeePosition;
import enterprise.dto.DepartmentDTO;
import enterprise.dto.EmployeeDTO;
import enterprise.persist.DepartmentsRepository;
import enterprise.persist.EmployeePositionsRepository;
import enterprise.persist.EmployeesRepository;
import enterprise.service.EmployeesService;

@RestController
@RequestMapping("/employees")
public class EmployeesController implements EmployeesService
{

    @Autowired
    DepartmentsRepository departmentsRepository;

    @Autowired
    EmployeesRepository employeesRepository;

    @Autowired
    EmployeePositionsRepository posiotionsRepository;

    @Autowired
    @Qualifier("employeeDTOValidator")
    Validator employeeValidator;

    @Override
    @RequestMapping(value = "/getemployees", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> getEmployees(@RequestBody DepartmentDTO department)
    {
        Department dep = departmentsRepository.get(department);
        if (dep == null)
            return new ResponseEntity<String>(DepartmentsController.DEPARTMENT_NOT_FOUND, HttpStatus.BAD_REQUEST);
        List<EmployeeDTO> result = dep.getEmployees()
                .stream()
                .map(e -> e.toDTO())
                .collect(Collectors.toList());
        return new ResponseEntity<List<EmployeeDTO>>(result, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody DepartmentDTO department, @Valid EmployeeDTO employee, BindingResult bindingResult)
    {
        employeeValidator.validate(employee, bindingResult);
        if (bindingResult.hasErrors())
            return new ResponseEntity<String>("Validation fails", HttpStatus.BAD_REQUEST);
        Department dep = departmentsRepository.get(department);
        if (dep == null)
            return new ResponseEntity<String>(DepartmentsController.DEPARTMENT_NOT_FOUND, HttpStatus.BAD_REQUEST);
        if (employee.getPosition() != null)
        {
            EmployeePosition position = posiotionsRepository.get(employee.getPosition().getName());
            if (position == null)
                return new ResponseEntity<String>("Position not found", HttpStatus.BAD_REQUEST);
            employee.setPosition(position);
        }
        Employee savedEmpl = employeesRepository.save(employee);
        dep.addEmployee(savedEmpl);
        departmentsRepository.save(dep);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> update(@RequestBody @Valid EmployeeDTO employee, BindingResult bindingResult)
    {
        employeeValidator.validate(employee, bindingResult);
        if (bindingResult.hasErrors())
            return new ResponseEntity<String>("Validation fails", HttpStatus.BAD_REQUEST);
        Employee saved = employeesRepository.get(employee);
        if (saved == null)
            return new ResponseEntity<String>(EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        if (employee.getPosition() != null)
        {
            EmployeePosition position = posiotionsRepository.get(employee.getPosition().getName());
            if (position == null)
                return new ResponseEntity<String>("Position not found", HttpStatus.BAD_REQUEST);
            employee.setPosition(position);
        }
        Department dep = saved.getDepartment();
        if (dep != null && dep.getChief() != null)
        {
            if (employee.isChief())
                new ResponseEntity<String>("Department already has chief", HttpStatus.BAD_REQUEST);
            if (employee.getSalary() != null && dep.getChief().getSalary() != null && employee.getSalary() > dep.getChief().getSalary())
                new ResponseEntity<String>("Employee's salary cant be greater then chief's salary", HttpStatus.BAD_REQUEST);
        }
        employeesRepository.save(employee);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/dissmis", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> dissmis(@RequestBody EmployeeDTO employee, LocalDateTime when)
    {
        Employee saved = employeesRepository.get(employee);
        if (saved == null)
            return new ResponseEntity<String>(EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        Department dep = saved.getDepartment();
        if (dep == null)
            return new ResponseEntity<String>("Employee has no department to dismiss", HttpStatus.BAD_REQUEST);
        dep.removeEmployee(saved);
        saved.setDismissalDate(when);
        departmentsRepository.save(dep);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getemployee", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> getEmployee(@RequestBody EmployeeDTO employee)
    {
        Employee saved = employeesRepository.get(employee);
        if (saved == null)
            return new ResponseEntity<String>(EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<EmployeeDTO>(saved.toDTO(), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/move", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> move(@RequestBody EmployeeDTO employee, DepartmentDTO where)
    {
        Employee saved = employeesRepository.get(employee);
        if (saved == null)
            return new ResponseEntity<String>(EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        Department dep = departmentsRepository.get(where);
        if (dep == null)
            return new ResponseEntity<String>("Target Department not found", HttpStatus.BAD_REQUEST);
        dep.addEmployee(saved);
        departmentsRepository.save(dep);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/moveall", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> moveAll(@RequestBody DepartmentDTO from, DepartmentDTO where)
    {
        Department sourceDep = departmentsRepository.get(from);
        if (sourceDep == null)
            return new ResponseEntity<String>("Source Department not found", HttpStatus.BAD_REQUEST);
        Department targetDep = departmentsRepository.get(from);
        if (targetDep == null)
            return new ResponseEntity<String>("Target Department not found", HttpStatus.BAD_REQUEST);
        if (sourceDep.getEmployeesCount() == 0)
            return new ResponseEntity<String>("Source Department has no employees to move", HttpStatus.BAD_REQUEST);
        for (Employee empl : sourceDep.getEmployees())
            targetDep.addEmployee(empl);
        departmentsRepository.save(targetDep);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getchief", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> getChief(@RequestBody EmployeeDTO employee)
    {
        Employee saved = employeesRepository.get(employee);
        if (saved == null)
            return new ResponseEntity<String>(EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        Department dep = saved.getDepartment();
        if (dep == null)
            return new ResponseEntity<String>("Employee has no department", HttpStatus.BAD_REQUEST);
        Employee chief = dep.getChief();
        if (chief != null)
            new ResponseEntity<EmployeeDTO>(chief.toDTO(), HttpStatus.OK);
        return new ResponseEntity<String>("Employee has no chief", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/findbundle", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> findBundle(@RequestBody EmployeeDTO qbe)
    {
        List<Employee> bundle = employeesRepository.getBundle(qbe);
        if (bundle != null)
        {
            List<EmployeeDTO> result = bundle
                    .stream()
                    .map(e -> e.toDTO())
                    .collect(Collectors.toList());
            new ResponseEntity<List<EmployeeDTO>>(result, HttpStatus.OK);
        }
        return new ResponseEntity<List<EmployeeDTO>>(new ArrayList<EmployeeDTO>(0), HttpStatus.OK);
    }

    public static final String EMPLOYEE_NOT_FOUND = "Employee not found.";

}
