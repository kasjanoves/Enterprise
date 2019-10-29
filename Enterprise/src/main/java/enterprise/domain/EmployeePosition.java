package enterprise.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class EmployeePosition
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    private EmployeePosition()
    {
    }

    private EmployeePosition(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
