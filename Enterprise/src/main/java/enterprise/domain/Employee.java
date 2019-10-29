package enterprise.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Employee
{
    @Id
    @GeneratedValue
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
    @OneToOne(mappedBy = "employee")
    private EmployeePosition position;
    private Double salary;
    private boolean isChief;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private Employee()
    {
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

    public void removeFromDepartment()
    {
        setDepartment(null);
    }

}
