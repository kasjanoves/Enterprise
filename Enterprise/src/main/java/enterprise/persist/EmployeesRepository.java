package enterprise.persist;

import java.util.List;

import enterprise.domain.Employee;
import enterprise.dto.EmployeeDTO;

public interface EmployeesRepository
{

    Employee save(EmployeeDTO employee);
    
    void save(Employee employee);
    
    Employee get(Employee employee);

    Employee get(EmployeeDTO qbe);
    
    List<Employee> getBundle(EmployeeDTO qbe);

    void delete(Employee employee);
}
