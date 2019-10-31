/*
* (c) LOIS, Ltd., 2019
* 
* $Id: DepartmentLogRepository.java,v 1.1 30 окт. 2019 г. 17:39:51 User Exp $
*/
package enterprise.persist;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import enterprise.domain.Department;
import enterprise.domain.DepartmentLogEntry;

@Repository
@Transactional
public class DepartmentLogRepositoryImpl implements DepartmentsLogRepository
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(DepartmentLogEntry entry)
    {
        entityManager.persist(entry);
    }

    @Override
    public List<DepartmentLogEntry> getAll(Department object)
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DepartmentLogEntry> query = builder.createQuery(DepartmentLogEntry.class);
        Root<DepartmentLogEntry> root = query.from(DepartmentLogEntry.class);
        Join<DepartmentLogEntry, Department> join = root.join("object");
        query.select(root).where(builder.equal(join.get("id"), object.getId()));
        return entityManager.createQuery(query).getResultList();
    }

}
