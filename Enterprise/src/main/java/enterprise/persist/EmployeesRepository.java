package enterprise.persist;

import enterprise.domain.Employee;
import enterprise.dto.EmployeeDTO;

public interface EmployeesRepository
{

    void save(EmployeeDTO employee);
    
    void save(Employee employee);

    Employee get(EmployeeDTO qbe);

    void delete(Employee employee);
}
