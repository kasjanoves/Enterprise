package enterprise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import enterprise.domain.Department;
import enterprise.domain.DepartmentLogEntry;
import enterprise.persist.DepartmentsLogRepository;
import enterprise.persist.DepartmentsRepository;

@Component
public class DepartmentsLoggerImpl implements DepartmentsLogger
{
    @Autowired
    DepartmentsLogRepository logRepo;

    @Autowired
    DepartmentsRepository departmentsRepo;

    @Override
    public void write(Department object, String message)
    {
        Department saved = departmentsRepo.get(object);
        if (saved != null)
        {
            DepartmentLogEntry entry = new DepartmentLogEntry(saved, message);
            logRepo.save(entry);
        }
    }

}
