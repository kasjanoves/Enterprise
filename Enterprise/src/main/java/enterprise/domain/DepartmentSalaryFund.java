package enterprise.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class DepartmentSalaryFund
{
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(mappedBy = "departmentSalaryFund")
    private Department department;
    private Double total;
    private LocalDateTime updateDate;

    public DepartmentSalaryFund()
    {
    }

    public DepartmentSalaryFund(Department department)
    {
        this.department = department;
    }

    public Double getTotal()
    {
        return total;
    }

    public void setTotal(Double total)
    {
        updateDate = LocalDateTime.now();
        this.total = total;
    }

    public LocalDateTime getUpdated()
    {
        return updateDate;
    }

    public Department getDepartment()
    {
        return department;
    }

}
