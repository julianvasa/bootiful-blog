package al.recipes.repositories;

import al.recipes.models.Recipes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipes, Long> {
    Page<Recipes> findAll(Pageable pageable);
    
    Page<Recipes> findAllBycategory_id(int categoryValue, Pageable pageable);

    /*
    public interface RecipeRepository extends JpaRepository<Recipes, Long> {
    List<Recipes> findAll();

    List<Recipes> findFirst10ByOrderByIdDesc();

    List<Recipes> findFirst2ByOrderByIdDesc();
    */
    
    @SuppressWarnings("unchecked")
    Recipes save(Recipes note);
    
}
