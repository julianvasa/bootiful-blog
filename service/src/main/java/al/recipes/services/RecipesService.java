package al.recipes.services;

import al.recipes.models.Recipes;
import al.recipes.repositories.RecipeRepository;
import al.recipes.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    public Page<Recipes> findAllBycategory(int cat, Pageable e) {
        return this.recipeRepo.findAllBycategory_id(cat, e);
    }
}
