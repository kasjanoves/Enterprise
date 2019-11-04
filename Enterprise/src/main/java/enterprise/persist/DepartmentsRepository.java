package enterprise.persist;

import java.util.List;

import enterprise.domain.Department;
import enterprise.dto.DepartmentDTO;

public interface DepartmentsRepository
{

    Department save(DepartmentDTO department);

    void save(Department department);

    Department get(DepartmentDTO qbe);

    Department get(Department department);

    List<Department> getAll();

    void delete(Department department);

}
