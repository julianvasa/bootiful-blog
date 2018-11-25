package al.recipes.services;

import al.recipes.models.Recipe;
import al.recipes.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecipesService {

    @Autowired
    private RecipeRepository recipeRepo;

    public Page<Recipe> findAll(Pageable e) {
        return this.recipeRepo.findAll(e);
    }

    public Page<Recipe> findAllBycategory(int cat, Pageable e) {
        return this.recipeRepo.findAllBycategory_id(cat, e);
    }
}
