package enterprise.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class DepartmentDTO
{
    private Long id;
    @NotNull
    private String name;
    @DateTimeFormat
    private LocalDateTime creationDate;
    private EmployeeDTO chief;
    private Integer employeesCount = 0;
    private DepartmentDTO parent;
    private Double salaryTotal;

    public DepartmentDTO()
    {
    }

    public DepartmentDTO(String name)
    {
        this.name = name;
    }

    public DepartmentDTO(DepartmentDTO parent, String name)
    {
        this(name);
        this.parent = parent;
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

    public Double getSalaryTotal()
    {
        return salaryTotal;
    }

    public void setSalaryTotal(Double salaryTotal)
    {
        this.salaryTotal = salaryTotal;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof DepartmentDTO)
            if (((DepartmentDTO) obj).getId() != null && getId() != null)
                return getId().equals(((DepartmentDTO) obj).getId());
        return false;
    }

    @Override
    public int hashCode()
    {
        return getId() == null ? 0 : getId().intValue();
    }

}
