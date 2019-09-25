package al.recipes.services;

import al.recipes.models.Ingredients;
import al.recipes.models.Recipes;
import al.recipes.models.Users;
import al.recipes.repositories.RecipeRepository;
import al.recipes.repositories.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecipesService {

    private final RecipeRepository recipeRepository;
    private final SearchRepository searchRepository;

    public RecipesService(RecipeRepository recipeRepository, SearchRepository searchRepository) {
        this.recipeRepository = recipeRepository;
        this.searchRepository = searchRepository;
    }

    public Recipes save(Recipes recipe) {
        return this.recipeRepository.save(recipe);
    }

    public List<Recipes> maxId() {
        return (List<Recipes>) this.recipeRepository.findAll(sortByIdDesc());
    }

    private Sort sortByIdDesc() {
        return new Sort(Sort.Direction.DESC, "id");
    }

    public List<Recipes> search(String filter) {
        return this.searchRepository.search(filter);
    }

    public Page<Recipes> findAll(Pageable e) {
        return this.recipeRepository.findAll(e);
    }

    public void setAuthor(long recipeId, Users author) {
        Optional<Recipes> r = this.recipeRepository.findById(recipeId);
        if (r.isPresent() && author != null) {
            r.get().setAuthor(author);
            this.recipeRepository.save(r.get());
        }
    }

    public Page<Recipes> findAllByCategory(int cat, Pageable e) {
        return this.recipeRepository.findAllByCategoryId(cat, e);
    }

    public Long maxIngrId() {
        List<Recipes> list = maxId();
        List<Ingredients> ingrs = list.get(0).getIngredients();
        ingrs.sort(Comparator.comparingLong(Ingredients::getId).reversed());
        return ingrs.get(0).getId();
    }

}
