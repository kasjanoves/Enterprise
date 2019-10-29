/*
* (c) LOIS, Ltd., 2019
* 
* $Id: LogEntry.java,v 1.1 28 окт. 2019 г. 14:40:41 User Exp $
*/
package enterprise.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

    private DepartmentLogEntry()
    {
        timeStamp = LocalDateTime.now();
    }

    private DepartmentLogEntry(String message, Department object)
    {
        this();
        this.message = message;
        this.object = object;
    }

    public String getMessage()
    {
        return message;
    }

    public LocalDateTime getTimestamp()
    {
        return timeStamp;
    }

    public Department getObject()
    {
        return object;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timeStamp = timestamp;
    }

    public void setObject(Department object)
    {
        this.object = object;
    }

}
