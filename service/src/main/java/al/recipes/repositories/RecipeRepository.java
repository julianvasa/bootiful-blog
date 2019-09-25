package al.recipes.repositories;

import al.recipes.models.Recipes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "recipes", collectionResourceRel = "recipes")
public interface RecipeRepository extends PagingAndSortingRepository<Recipes, Long> {
    Page<Recipes> findAll(Pageable pageable);

    Page<Recipes> findAllByCategoryId(int categoryValue, Pageable pageable);

    @SuppressWarnings("unchecked")
    Recipes save(Recipes note);

}
