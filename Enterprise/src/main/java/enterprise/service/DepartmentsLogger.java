package enterprise.service;

import enterprise.domain.Department;

public interface DepartmentsLogger
{
    void write(Department object, String message);
}
