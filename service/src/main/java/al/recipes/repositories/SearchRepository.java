package al.recipes.repositories;

import al.recipes.models.Recipes;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Recipes> search(String filter) {
        String hql = "SELECT r from Recipes as r where name like '%" + filter + "%' or intro like '%" + filter + "%'" +
                " or instruction like '%" + filter + "%'";
        return (List<Recipes>) entityManager.createQuery(hql).getResultList();
    }
}
