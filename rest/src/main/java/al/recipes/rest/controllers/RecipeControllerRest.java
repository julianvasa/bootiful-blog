package al.recipes.rest.controllers;

import al.recipes.models.Recipes;
import al.recipes.services.CategoriesService;
import al.recipes.services.RecipesService;
import al.recipes.services.SingleRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RecipeControllerRest {

    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 20;
    @Autowired
    RecipesService recipesService;
    @Autowired
    SingleRecipeService singleRecipeService;
    @Autowired
    CategoriesService categoriesService;

    @GetMapping("/recipes/{page}")
    public Page<Recipes> getAllRecipes(@PathVariable(value = "page") Integer page) {
        int evalPage = (page < 1) ? INITIAL_PAGE : page - 1;
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));

        return recipesService.findAll(new PageRequest(evalPage, INITIAL_PAGE_SIZE, sort));
    }

    @GetMapping("/recipe/{id}")
    public Optional<Recipes> getRecipeById(@PathVariable(value = "id") long recipeId) {
        return singleRecipeService.findById(recipeId);
    }

    @GetMapping("/recipes/{page}/cat/{cat}")
    public Page<Recipes> getAllRecipesByCat(@PathVariable(value = "page") Integer page, @PathVariable(value = "cat") Integer cat) {
        int evalPage = (page < 1) ? INITIAL_PAGE : page - 1;
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));

        return recipesService.findAllBycategory(cat, new PageRequest(evalPage, INITIAL_PAGE_SIZE, sort));
    }

    @GetMapping("/search/{keyword}")
    public List<Recipes> search(@PathVariable(value = "keyword") String keyword) {
        return recipesService.search(keyword);
    }

/*
    @PostMapping("/recipes")
    public Recipes createRecipe(@Valid @RequestBody Recipes recipe) {
        return recipesService.save(recipe);
    }



    @PutMapping("/recipes/{id}")
    public Recipes updateRecipe(@PathVariable(value = "id") Long recipeId, @Valid @RequestBody Recipes recipeDetails) {

        Recipes note = recipesService.findById(recipeId);

        note.setName(recipeDetails.getName());
        note.setInstruction(recipeDetails.getInstruction());

        return recipesService.save(note);
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable(value = "id") Long recipeId) {
        Recipes recipe = recipesService.findById(recipeId);

        recipesService.delete(recipe);

        return ResponseEntity.ok().build();
    }*/
}
