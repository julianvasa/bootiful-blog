package al.recipes.services;

import al.recipes.models.Ingredients;
import al.recipes.models.Recipes;
import al.recipes.models.Users;
import al.recipes.repositories.RecipeRepository;
import al.recipes.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecipesService {
    
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private SearchRepository searchRepository;
    
    public Recipes save(Recipes recipe) {
        return this.recipeRepo.save(recipe);
    }
    
    public List<Recipes> maxId() {
        return (List<Recipes>) this.recipeRepo.findAll(sortByIdDesc());
    }
    
    private Sort sortByIdDesc() {
        return new Sort(Sort.Direction.DESC, "id");
    }
    
    public List<Recipes> search(String filter) {
        return this.searchRepository.search(filter);
    }
    
    public Page<Recipes> findAll(Pageable e) {
        return this.recipeRepo.findAll(e);
    }
    
    public void updateUser(long recipeid, Users u) {
        Optional<Recipes> r = this.recipeRepo.findById(recipeid);
        if (r.isPresent() && u != null) {
            r.get().setUser(u);
            this.recipeRepo.save(r.get());
        }
    }
    
    public Page<Recipes> findAllBycategory(int cat, Pageable e) {
        return this.recipeRepo.findAllBycategory_id(cat, e);
    }
    
    public Long maxIngrId() {
        List<Recipes> list = maxId();
        List<Ingredients> ingrs = list.get(0).getIngredients();
        ingrs.sort(Comparator.comparingLong(Ingredients::getId).reversed());
        return ingrs.get(0).getId();
    }
    
}
