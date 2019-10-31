package enterprise.persist;

import enterprise.domain.Department;
import enterprise.dto.DepartmentDTO;

public interface DepartmentsRepository
{

    void save(DepartmentDTO department);
    
    void save(Department department);

    Department get(DepartmentDTO qbe);
    
    Department get(Department department);
    
    void delete(Department department);

}
