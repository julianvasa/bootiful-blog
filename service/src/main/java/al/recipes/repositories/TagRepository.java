package al.recipes.repositories;

import al.recipes.models.Tags;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class TagRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    @SuppressWarnings("unchecked")
    public List<Tags> getRandomTags() {
        String hql = "SELECT value from Tags as r group by r.value having count(r.value)>1 order by RAND()";
        return (List<Tags>) entityManager.createQuery(hql).getResultList();//.subList(0, 20);
    }
}
