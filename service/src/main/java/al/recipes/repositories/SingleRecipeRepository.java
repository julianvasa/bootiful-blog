package al.recipes.repositories;

import al.recipes.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleRecipeRepository extends JpaRepository<Recipe, Long> {

}
