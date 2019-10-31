package enterprise.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table
public class EmployeePosition
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public EmployeePosition()
    {
    }

    public EmployeePosition(String name)
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
    
    @Override
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj, "name");
    }

    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this, "name");
    }

}
