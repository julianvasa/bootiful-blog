package al.recipes.services;

import al.recipes.models.Recipes;
import al.recipes.repositories.SingleRecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SingleRecipeService {

    private final SingleRecipeRepository singleRecipeRepository;

    public SingleRecipeService(SingleRecipeRepository singleRecipeRepository) {
        this.singleRecipeRepository = singleRecipeRepository;
    }

    public Optional<Recipes> findById(long id) {
        return this.singleRecipeRepository.findById(id);
    }
}
