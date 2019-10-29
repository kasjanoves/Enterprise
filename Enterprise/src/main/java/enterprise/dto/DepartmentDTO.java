package enterprise.dto;

import java.time.LocalDateTime;

public class DepartmentDTO
{
    private Long id;
    private String name;
    private LocalDateTime creationDate;
    private EmployeeDTO chief;
    private Integer employeesCount;
    private DepartmentDTO parent;

    private DepartmentDTO()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public EmployeeDTO getChief()
    {
        return chief;
    }

    public void setChief(EmployeeDTO chief)
    {
        this.chief = chief;
    }

    public Integer getEmployeesCount()
    {
        return employeesCount;
    }

    public void setEmployeesCount(Integer employeesCount)
    {
        this.employeesCount = employeesCount;
    }

    public DepartmentDTO getParent()
    {
        return parent;
    }

    public void setParent(DepartmentDTO parent)
    {
        this.parent = parent;
    }

}
