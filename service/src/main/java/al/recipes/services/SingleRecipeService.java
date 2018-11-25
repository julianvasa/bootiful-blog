package al.recipes.services;

import al.recipes.models.Recipe;
import al.recipes.repositories.SingleRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SingleRecipeService {

    @Autowired
    private SingleRecipeRepository recipeRepo;

    public Optional<Recipe> findById(long id) {
        return this.recipeRepo.findById(id);
    }

    /*
    public List<Recipe> findLatest10() {
        return this.recipeRepo.findFirst10ByOrderByIdDesc();
    }

    public Recipe save(Recipe note) {
        return this.recipeRepo.save(note);
    }

    public void delete(Recipe recipe) {
    	this.recipeRepo.delete(recipe);
    }*/
}
