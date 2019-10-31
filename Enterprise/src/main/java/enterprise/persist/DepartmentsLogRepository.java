package enterprise.persist;

import java.util.List;

import enterprise.domain.Department;
import enterprise.domain.DepartmentLogEntry;

public interface DepartmentsLogRepository
{
    void save(DepartmentLogEntry entry);

    List<DepartmentLogEntry> getAll(Department object);
}
