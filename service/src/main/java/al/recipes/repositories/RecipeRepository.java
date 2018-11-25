package al.recipes.repositories;

import al.recipes.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {
    Page<Recipe> findAll(Pageable pageable);

    Page<Recipe> findAllBycategory_id(int categoryValue, Pageable pageable);

    /*
    public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAll();

    List<Recipe> findFirst10ByOrderByIdDesc();

    List<Recipe> findFirst2ByOrderByIdDesc();

    @SuppressWarnings("unchecked")
    Recipe save(Recipe note);
    */
}
