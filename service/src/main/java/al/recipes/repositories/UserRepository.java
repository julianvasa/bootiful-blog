package al.recipes.repositories;

import al.recipes.models.Users;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Users getUserById(long userId) {
        return entityManager.find(Users.class, userId);
    }

    @SuppressWarnings("unchecked")
    public List<Users> getAllUsers() {
        String hql = "FROM Users as u ORDER BY u.id";
        return (List<Users>) entityManager.createQuery(hql).getResultList();
    }

    public void addUser(Users users) {
        entityManager.persist(users);
    }


    public void updateUser(Users users) {
        Users u = getUserById(users.getId());
        u.setFullName(users.getFullName());
        u.setUsername(users.getUsername());
        u.setPassword(users.getPassword());
        entityManager.flush();
    }

    public Users getUser(String username) {
        String hql = "FROM Users as u where u.username='" + username + "' ORDER BY u.id";
        return (Users) entityManager.createQuery(hql).getResultList();
    }

    public void deleteUser(long userId) {
        entityManager.remove(getUserById(userId));
    }
} 

