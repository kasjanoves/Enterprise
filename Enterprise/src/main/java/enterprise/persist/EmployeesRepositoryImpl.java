/*
* (c) LOIS, Ltd., 2019
* 
* $Id: EmployeesRepositoryImpl.java,v 1.1 29 окт. 2019 г. 22:56:58 User Exp $
*/
package enterprise.persist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import enterprise.domain.Employee;
import enterprise.dto.EmployeeDTO;

@Repository
@Transactional
public class EmployeesRepositoryImpl implements EmployeesRepository
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Employee save(EmployeeDTO dto)
    {
        Employee saved = get(dto);
        if (saved == null)
        {
            Employee newOne = new Employee();
            newOne.fill(dto);
            entityManager.persist(newOne);
            return newOne;
        }
        else
        {
            saved.fill(dto);
            return entityManager.merge(saved);
        }
    }

    @Override
    public void save(Employee employee)
    {
        if (employee.getId() == null)
        {
            entityManager.persist(employee);
        }
        else
        {
            entityManager.merge(employee);
        }
    }

    @Override
    public Employee get(EmployeeDTO qbe)
    {
        if (qbe != null)
        {
            if (qbe.getId() != null)
                return entityManager.find(Employee.class, qbe.getId());
            CriteriaQuery<Employee> query = buildQuery(qbe);
            if (query == null)
                return null;
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
        return null;
    }

    private CriteriaQuery<Employee> buildQuery(EmployeeDTO qbe)
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (qbe.getName() != null)
            predicates.add(builder.equal(root.get("name"), qbe.getName()));
        if (qbe.getLastName() != null)
            predicates.add(builder.equal(root.get("lastName"), qbe.getLastName()));
        if (qbe.getPatronymic() != null)
            predicates.add(builder.equal(root.get("patronymic"), qbe.getPatronymic()));
        if (qbe.getBornDate() != null)
            predicates.add(builder.equal(root.get("bornDate"), qbe.getBornDate()));
        if (predicates.size() == 1)
            query.select(root).where(predicates.get(0));
        else if (predicates.size() > 1)
            return query.select(root).where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        return null;
    }

    @Override
    public void delete(Employee employee)
    {
        if (employee.getId() != null)
        {
            Employee saved = entityManager.find(Employee.class, employee.getId());
            if (saved != null)
            {
                if (saved.getDepartment() != null)
                    saved.getDepartment().removeEmployee(saved);
                entityManager.remove(saved);
            }
        }
    }

    @Override
    public Employee get(Employee employee)
    {
        return entityManager.find(Employee.class, employee.getId());
    }

    @Override
    public List<Employee> getBundle(EmployeeDTO qbe)
    {
        if (qbe != null)
        {
            CriteriaQuery<Employee> query = buildQuery(qbe);
            if (query == null)
                return null;
            try
            {
                return entityManager.createQuery(query).getResultList();
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
                return null;
            }
        }
        return null;
    }

}
