package enterprise.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import enterprise.dto.EmployeeDTO;

@Entity
@Table
public class Employee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String name;
    private String patronymic;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime bornDate;
    private String phone;
    private String email;
    private LocalDateTime employmentDate;
    private LocalDateTime dismissalDate;
    @OneToOne
    @JoinColumn(name="position_id")
    private EmployeePosition position;
    private Double salary;
    private boolean isChief;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee()
    {
    }

    public Long getId()
    {
        return id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPatronymic()
    {
        return patronymic;
    }

    public void setPatronymic(String patronymic)
    {
        this.patronymic = patronymic;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public LocalDateTime getBornDate()
    {
        return bornDate;
    }

    public void setBornDate(LocalDateTime bornDate)
    {
        this.bornDate = bornDate;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public LocalDateTime getEmploymentDate()
    {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDateTime employmentDate)
    {
        this.employmentDate = employmentDate;
    }

    public LocalDateTime getDismissalDate()
    {
        return dismissalDate;
    }

    public void setDismissalDate(LocalDateTime dismissalDate)
    {
        this.dismissalDate = dismissalDate;
    }

    public EmployeePosition getPosition()
    {
        return position;
    }

    public void setPosition(EmployeePosition position)
    {
        this.position = position;
    }

    public Double getSalary()
    {
        return salary;
    }

    public void setSalary(Double salary)
    {
        this.salary = salary;
    }

    public boolean isChief()
    {
        return isChief;
    }

    public void setChief(boolean isChief)
    {
        this.isChief = isChief;
    }

    public Department getDepartment()
    {
        return department;
    }

    protected void setDepartment(Department department)
    {
        this.department = department;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Employee)
            if (((Employee) obj).getId() != null && getId() != null)
                return getId().equals(((Employee) obj).getId());
        return false;
    }

    @Override
    public int hashCode()
    {
        return getId() == null ? 0 : getId().intValue();
    }

    public void removeFromDepartment()
    {
        setDepartment(null);
    }

    public EmployeeDTO toDTO()
    {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(getId());
        dto.setName(getName());
        dto.setLastName(getLastName());
        dto.setPatronymic(getPatronymic());
        dto.setBornDate(getBornDate());
        dto.setGender(getGender());
        dto.setEmail(getEmail());
        dto.setPhone(getPhone());
        dto.setPosition(getPosition());
        dto.setChief(isChief());
        dto.setEmploymentDate(getEmploymentDate());
        dto.setDismissalDate(getDismissalDate());
        dto.setSalary(getSalary());
        return dto;
    }

    public void fill(EmployeeDTO dto)
    {
        setName(dto.getName());
        setLastName(dto.getLastName());
        setPatronymic(dto.getPatronymic());
        setBornDate(dto.getBornDate());
        setGender(dto.getGender());
        setEmail(dto.getEmail());
        setPhone(dto.getPhone());
        setPosition(dto.getPosition());
        setChief(dto.isChief());
        setEmploymentDate(dto.getEmploymentDate());
        setDismissalDate(dto.getDismissalDate());
        setSalary(dto.getSalary());
        
    }

}
