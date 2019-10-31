package enterprise.persist;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import enterprise.domain.EmployeePosition;

@Repository
@Transactional
public class EmployeePositionsRepositoryImpl implements EmployeePositionsRepository
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(String name)
    {
        entityManager.persist(new EmployeePosition(name));
    }

    @Override
    public EmployeePosition get(String name)
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeePosition> query = builder.createQuery(EmployeePosition.class);
        Root<EmployeePosition> root = query.from(EmployeePosition.class);
        query.select(root).where(builder.equal(root.get("name"), name));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<EmployeePosition> getAll()
    {
        CriteriaQuery<EmployeePosition> criteria = entityManager.getCriteriaBuilder()
                .createQuery(EmployeePosition.class);
        criteria.from(EmployeePosition.class);
        return entityManager.createQuery(criteria).getResultList();
    }

}
