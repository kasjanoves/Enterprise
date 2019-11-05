package enterprise;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import enterprise.domain.Department;
import enterprise.persist.DepartmentsRepository;

@Component
public class ScheduledSalaryFundsUpdater
{
    private static final Logger log = LoggerFactory.getLogger(ScheduledSalaryFundsUpdater.class);

    @Autowired
    DepartmentsRepository departmentsRepository;

    @Scheduled(fixedRate = 30000)
    public void updateFunds()
    {
        log.info("Started at " + LocalDateTime.now());
        try
        {
            List<Department> allDepartments = departmentsRepository.getAll();
            for (Department dep : allDepartments)
            {
                dep.updateSalaryTotal();
                departmentsRepository.save(dep);
            }
        }
        catch (Exception e)
        {
            log.info("Excecution failed: " + e.getMessage());
            return;
        }

        log.info("Finished at " + LocalDateTime.now());

    }
}
