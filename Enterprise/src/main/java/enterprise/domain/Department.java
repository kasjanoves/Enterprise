package enterprise.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import enterprise.dto.DepartmentDTO;

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
    @OneToMany(mappedBy = "parentDep", fetch = FetchType.EAGER)
    private List<Department> subDeps = new ArrayList<Department>(0);
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    private List<Employee> employees = new ArrayList<Employee>(0);
    @OneToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.REMOVE
    })
    @JoinColumn(name = "fund_id")
    private DepartmentSalaryFund salaryFund;

    public Department()
    {
        salaryFund = new DepartmentSalaryFund(this);
    }

    public Department(String name)
    {
        this();
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

    public void fill(DepartmentDTO dto)
    {
        setName(dto.getName());
        setCreationDate(dto.getCreationDate());
    }

    public DepartmentDTO toDTO()
    {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(getId());
        dto.setName(getName());
        dto.setCreationDate(getCreationDate());
        if (getParent() != null)
            dto.setParent(getParent().toDTO());
        dto.setEmployeesCount(getEmployeesCount());
        dto.setSalaryTotal(salaryFund.getTotal());
        return dto;
    }

    public List<Department> getParentsRecursively()
    {
        ArrayList<Department> result = new ArrayList<Department>();
        collectParentsRecursively(result);
        return result;
    }

    private void collectParentsRecursively(ArrayList<Department> result)
    {
        if (getParent() == null)
            return;
        result.add(getParent());
        getParent().collectParentsRecursively(result);

    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Department)
            if (((Department) obj).getId() != null && getId() != null)
                return getId().equals(((Department) obj).getId());
        return false;
    }

    @Override
    public int hashCode()
    {
        return getId() == null ? 0 : getId().intValue();
    }

    public Department getParentDep()
    {
        return parentDep;
    }

    public List<Employee> getEmployees()
    {
        return Collections.unmodifiableList(employees);
    }

    public void addEmployee(Employee employee)
    {
        employees.add(employee);
        if (employee.getDepartment() != null)
            employee.getDepartment().removeEmployee(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee)
    {
        if (employees.contains(employee))
        {
            employees.remove(employee);
            employee.setDepartment(null);
        }
    }

}
