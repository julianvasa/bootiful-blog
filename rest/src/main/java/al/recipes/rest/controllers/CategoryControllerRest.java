package al.recipes.rest.controllers;

import al.recipes.models.Categories;
import al.recipes.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryControllerRest {

    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 20;
    @Autowired
    CategoriesService categoriesService;

    @GetMapping("/categories")
    public List<Categories> getAllCategories() {
        return categoriesService.findAll();
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
