package al.recipes.services;

import al.recipes.models.Recipes;
import al.recipes.repositories.SingleRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SingleRecipeService {

    @Autowired
    private SingleRecipeRepository recipeRepo;

    public Optional<Recipes> findById(long id) {
        return this.recipeRepo.findById(id);
    }

    /*
    public List<Recipes> findLatest10() {
        return this.recipeRepo.findFirst10ByOrderByIdDesc();
    }

    public Recipes save(Recipes note) {
        return this.recipeRepo.save(note);
    }

    public void delete(Recipes recipe) {
    	this.recipeRepo.delete(recipe);
    }*/
}
