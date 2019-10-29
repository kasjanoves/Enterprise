package enterprise.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Department
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "parentdep_id")
    private Department parentDep;
    @OneToMany(mappedBy = "department")
    private List<Department> subDeps = new ArrayList<Department>(0);
    @OneToMany(mappedBy = "employee")
    private List<Employee> employees = new ArrayList<Employee>(0);
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "fund_id")
    private DepartmentSalaryFund salaryFund;

    public Department()
    {
        salaryFund = new DepartmentSalaryFund(this);
    }

    private Department(String name)
    {
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    public int getEmployeesCount()
    {
        return employees.size();

    }

    public Employee getChief()
    {

        return employees.stream()
                .filter(e -> e.isChief())
                .findFirst()
                .get();
    }

    public List<Department> getSubDepartments()
    {
        return Collections.unmodifiableList(subDeps);
    }

    public List<Department> getSubDepsRecursively()
    {
        List<Department> result = new ArrayList<Department>();
        collectAllSubDepsRecursively(result);
        return result;
    }

    private void collectAllSubDepsRecursively(List<Department> result)
    {
        for (Department dep : subDeps)
        {
            result.add(dep);
            dep.collectAllSubDepsRecursively(result);
        }
    }

    public Department getParent()
    {
        return parentDep;
    }

    protected void setParentDep(Department parentDep)
    {
        this.parentDep = parentDep;
    }

    public void removeSubDep(Department department)
    {
        if (subDeps.contains(department))
        {
            subDeps.remove(department);
            department.setParentDep(null);
        }
    }

    public void detachSubDeps()
    {
        for (Department dep : subDeps)
            dep.setParentDep(null);
        subDeps.clear();

    }

    public void releaseEmployees()
    {
        for (Employee empl : employees)
            empl.removeFromDepartment();
        employees.clear();
    }

    public void addDep(Department department)
    {
        subDeps.add(department);
        department.setParentDep(this);
    }
}
