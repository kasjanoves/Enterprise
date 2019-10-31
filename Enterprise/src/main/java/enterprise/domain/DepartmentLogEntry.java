package enterprise.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table
public class DepartmentLogEntry
{
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private LocalDateTime timeStamp;
    @OneToOne()
    @JoinColumn(name = "object_id")
    private Department object;

    public DepartmentLogEntry()
    {
        timeStamp = LocalDateTime.now();
    }

    public DepartmentLogEntry(Department object, String message)
    {
        this();
        this.message = message;
        this.object = object;
    }
    
    public Department getObject()
    {
        return object;
    }

    public Long getId()
    {
        return id;
    }

    public String getMessage()
    {
        return message;
    }

    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj, "id");
    }

    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this, "id");
    }

}
