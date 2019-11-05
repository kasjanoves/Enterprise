package enterprise.persist;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import enterprise.domain.Department;
import enterprise.dto.DepartmentDTO;

@Repository
@Transactional
public class DepartmentsRepositoryImpl implements DepartmentsRepository
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Department save(DepartmentDTO dto)
    {
        Department saved = get(dto);
        if (saved == null)
        {
            Department newOne = new Department();
            newOne.fill(dto);
            entityManager.persist(newOne);
            if (dto.getParent() != null)
            {
                Department parent = get(dto.getParent());
                if (parent != null)
                    parent.addDep(newOne);
            }
            return newOne;
        }
        else
        {
            saved.fill(dto);
            Department oldParent = saved.getParent();
            if (dto.getParent() != null)
            {
                Department newParent = get(dto.getParent());
                if (newParent != null)
                {
                    if (oldParent != null && !newParent.equals(oldParent))
                        oldParent.removeSubDep(saved);
                    newParent.addDep(saved);
                }

            }
            else
            {
                if (oldParent != null)
                    oldParent.removeSubDep(saved);
            }
            return entityManager.merge(saved);
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
                try
                {
                    return entityManager.createQuery(query).getSingleResult();
                }
                catch (Exception e)
                {
                    System.out.println(e.getStackTrace());
                    return null;
                }

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
                entityManager.remove(saved);
            }
        }
    }

    @Override
    public Department get(Department department)
    {
        return entityManager.find(Department.class, department.getId());
    }

    @Override
    public List<Department> getAll()
    {
        CriteriaQuery<Department> criteria = entityManager.getCriteriaBuilder()
                .createQuery(Department.class);
        criteria.from(Department.class);
        return entityManager.createQuery(criteria).getResultList();
    }

}
