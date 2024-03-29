package enterprise.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import enterprise.domain.EmployeePosition;
import enterprise.domain.Gender;

public class EmployeeDTO
{
    private Long id;

    @Pattern(regexp = RUS_FIO_PATTERN)
    private String lastName;
    @Pattern(regexp = RUS_FIO_PATTERN)
    private String name;
    @Pattern(regexp = RUS_FIO_PATTERN)
    private String patronymic;
    @ValueOfEnum(enumClass = Gender.class)
    private Gender gender;
    @DateTimeFormat
    private LocalDateTime bornDate;
    @Pattern(regexp = PHONE_PATTERN)
    private String phone;
    @Email
    private String email;
    @DateTimeFormat
    private LocalDateTime employmentDate;
    @DateTimeFormat
    private LocalDateTime dismissalDate;
    private EmployeePosition position;
    @Digits(fraction = 2, integer = 6)
    private Double salary;
    private boolean isChief;

    public EmployeeDTO()
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

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof EmployeeDTO)
            if (((EmployeeDTO) obj).getId() != null && getId() != null)
                return getId().equals(((EmployeeDTO) obj).getId());
        return false;

    }

    @Override
    public int hashCode()
    {
        return getId() == null ? 0 : getId().intValue();
    }

    private static final String RUS_FIO_PATTERN = "[а-яА-Я-]";
    private static final String PHONE_PATTERN = "[0-9+-() ]";
}
