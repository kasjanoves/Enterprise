package enterprise.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import enterprise.domain.Department;
import enterprise.dto.DepartmentDTO;
import enterprise.persist.DepartmentsRepository;
import enterprise.service.DepartmentsLogger;
import enterprise.service.DepartmentsService;

@RestController
@RequestMapping("/departments")
public class DepartmentsController implements DepartmentsService
{

    @Autowired
    DepartmentsRepository departmentsRepository;

    @Autowired
    DepartmentsLogger departmentsLogger;

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody @Valid DepartmentDTO department, Errors errors)
    {
        if (errors.hasErrors())
            return new ResponseEntity<String>("Validation fails", HttpStatus.BAD_REQUEST);
        if (department.getParent() != null)
        {
            Department parent = departmentsRepository.get(department.getParent());
            if (parent == null)
                return new ResponseEntity<String>(PARENT_DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Department exists = departmentsRepository.get(new DepartmentDTO(department.getName()));
        if (exists != null)
            return new ResponseEntity<String>("Department with same name already exists", HttpStatus.BAD_REQUEST);
        departmentsRepository.save(department);
        departmentsLogger.write(departmentsRepository.get(department), "Created");
        return new ResponseEntity<String>("Created successfully!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/rename", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> rename(@RequestBody DepartmentDTO department, String name)
    {
        Department exists = departmentsRepository.get(department);
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        Department byName = departmentsRepository.get(new DepartmentDTO(name));
        if (byName != null)
            return new ResponseEntity<String>("Department with same name already exists", HttpStatus.BAD_REQUEST);
        departmentsLogger.write(exists, "Renamed " + exists.getName() + " -> " + name);
        exists.setName(name);
        departmentsRepository.save(exists);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> delete(@RequestBody DepartmentDTO department)
    {
        Department exists = departmentsRepository.get(department);
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (exists.getEmployeesCount() > 0)
            return new ResponseEntity<String>("Department has employees. Cant be deleted", HttpStatus.BAD_REQUEST);
        departmentsRepository.delete(exists);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/get", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> get(@RequestBody DepartmentDTO department)
    {
        Department exists = departmentsRepository.get(department);
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new ResponseEntity<DepartmentDTO>(exists.toDTO(), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getsubdeps", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> getSubDepartments(@RequestBody DepartmentDTO department)
    {
        Department exists = departmentsRepository.get(department);
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<DepartmentDTO> result = exists.getSubDepartments()
                .stream()
                .map(d -> d.toDTO())
                .collect(Collectors.toList());
        return new ResponseEntity<List<DepartmentDTO>>(result, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getsubdepsrecursive", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> getSubDepartmentsRecursively(@RequestBody DepartmentDTO department)
    {
        Department exists = departmentsRepository.get(department);
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<DepartmentDTO> result = exists.getSubDepsRecursively()
                .stream()
                .map(d -> d.toDTO())
                .collect(Collectors.toList());
        return new ResponseEntity<List<DepartmentDTO>>(result, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/move", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> move(@RequestBody DepartmentDTO what, DepartmentDTO were)
    {
        Department source = departmentsRepository.get(what);
        if (source == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        Department target = departmentsRepository.get(were);
        if (target == null)
            return new ResponseEntity<String>("Target department not found", HttpStatus.NOT_FOUND);
        if (source.equals(target))
            return new ResponseEntity<String>("Source and target must be different", HttpStatus.BAD_REQUEST);
        List<Department> subDeps = source.getSubDepsRecursively();
        if (subDeps.contains(target))
            return new ResponseEntity<String>("Target department must not be in Source sub departments", HttpStatus.BAD_REQUEST);
        departmentsLogger.write(source, "moved from " + source.getParent() + " to " + target);
        what.setParent(were);
        departmentsRepository.save(what);
        return new ResponseEntity<String>("Success!", HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getparents", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> getParents(@RequestBody DepartmentDTO department)
    {
        Department exists = departmentsRepository.get(department);
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<DepartmentDTO> result = exists.getParentsRecursively()
                .stream()
                .map(d -> d.toDTO())
                .collect(Collectors.toList());
        return new ResponseEntity<List<DepartmentDTO>>(result, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/findbyname", method = RequestMethod.GET)
    public ResponseEntity<?> findByName(String name)
    {
        Department exists = departmentsRepository.get(new DepartmentDTO(name));
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new ResponseEntity<DepartmentDTO>(exists.toDTO(), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getsalarytotal", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> getSalaryTotal(@RequestBody DepartmentDTO department)
    {
        Department exists = departmentsRepository.get(department);
        if (exists == null)
            return new ResponseEntity<String>(DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new ResponseEntity<Double>(exists.getSalaryFundTotal(), HttpStatus.OK);
    }

    public static final String DEPARTMENT_NOT_FOUND = "Department not found";
    public static final String PARENT_DEPARTMENT_NOT_FOUND = "Parent department not found";

}
