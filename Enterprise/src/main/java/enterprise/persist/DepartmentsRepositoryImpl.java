/*
* (c) LOIS, Ltd., 2019
* 
* $Id: DepartmentsRepositoryImpl.java,v 1.1 28 окт. 2019 г. 22:31:59 User Exp $
*/
package enterprise.persist;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import enterprise.domain.Department;
import enterprise.dto.DepartmentDTO;

@Repository
public class DepartmentsRepositoryImpl implements DepartmentsRepository
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(DepartmentDTO dto)
    {
        Department saved = get(dto);
        if (saved == null)
        {
            Department newOne = new Department();
            //TODO ModelMapper
            entityManager.persist(newOne);
            if (dto.getParent() != null)
            {
                Department parent = get(dto.getParent());
                if (parent != null)
                    parent.addDep(newOne);
            }
        }
        else
        {
            //TODO ModelMapper
            entityManager.merge(saved);
        }
    }

    @Override
    public void save(Department department)
    {
        if (department.getId() == null)
        {
            entityManager.persist(department);
        }
        else
        {
            entityManager.merge(department);
        }

    }

    @Override
    public Department get(DepartmentDTO qbe)
    {
        if (qbe != null)
        {
            if (qbe.getId() != null)
                return entityManager.find(Department.class, qbe.getId());
            if (qbe.getName() != null && !qbe.getName().isEmpty())
            {
                CriteriaBuilder builder = entityManager.getCriteriaBuilder();
                CriteriaQuery<Department> query = builder.createQuery(Department.class);
                Root<Department> root = query.from(Department.class);
                query.select(root).where(builder.equal(root.get("name"), qbe.getName()));
                return entityManager.createQuery(query).getSingleResult();
            }

        }
        return null;
    }

    @Override
    public void delete(Department department)
    {
        if (department.getId() != null)
        {
            Department saved = entityManager.find(Department.class, department.getId());
            if (saved != null)
            {
                if (saved.getParent() != null)
                    saved.getParent().removeSubDep(saved);
                saved.detachSubDeps();
                saved.releaseEmployees();
                entityManager.remove(department);
            }
        }
    }

}
