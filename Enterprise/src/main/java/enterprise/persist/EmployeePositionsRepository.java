package enterprise.persist;

import java.util.List;

import enterprise.domain.EmployeePosition;

public interface EmployeePositionsRepository
{
    void save(String name);

    EmployeePosition get(String name);

    List<EmployeePosition> getAll();
}
