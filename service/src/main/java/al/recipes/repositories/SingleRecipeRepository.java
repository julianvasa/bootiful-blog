package al.recipes.repositories;

import al.recipes.models.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleRecipeRepository extends JpaRepository<Recipes, Long> {

}
