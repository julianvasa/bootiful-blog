package al.recipes.rest.controllers;

import al.recipes.models.Categories;
import al.recipes.models.Recipes;
import al.recipes.models.Users;
import al.recipes.services.CategoriesService;
import al.recipes.services.RecipesService;
import al.recipes.services.SingleRecipeService;
import al.recipes.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    UserService usersService;
    
    @GetMapping("/recipes/{page}")
    @ApiOperation(value = "Get recipes by page", notes = "Get recipes by page")
    public Page<Recipes> getAllRecipes(@PathVariable(value = "page") Integer page) {
        /*if (!SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal().equals("anonymousUser")) {
            int evalPage = (page < 1) ? INITIAL_PAGE : page - 1;
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
            return recipesService.findAll(new PageRequest(evalPage, INITIAL_PAGE_SIZE, sort));
        } else return null;*/
        int evalPage = (page < 1) ? INITIAL_PAGE : page - 1;
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        return recipesService.findAll(new PageRequest(evalPage, INITIAL_PAGE_SIZE, sort));
    }
    
    @GetMapping("/recipe/{id}")
    @ApiOperation(value = "Get recipe by id", notes = "Get recipe by id")
    public Optional<Recipes> getRecipeById(@PathVariable(value = "id") long recipeId) {
        return singleRecipeService.findById(recipeId);
    }
    
    @GetMapping("/recipes/{page}/cat/{cat}")
    @ApiOperation(value = "Get recipes by category", notes = "Get recipes by category")
    public Page<Recipes> getAllRecipesByCat(@PathVariable(value = "page") Integer page, @PathVariable(value = "cat") Integer cat) {
        int evalPage = (page < 1) ? INITIAL_PAGE : page - 1;
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        return recipesService.findAllBycategory(cat, new PageRequest(evalPage, INITIAL_PAGE_SIZE, sort));
    }
    
    @GetMapping("/search/{keyword}")
    @ApiOperation(value = "Search for recipes", notes = "Search for recipes")
    public List<Recipes> search(@PathVariable(value = "keyword") String keyword) {
        return recipesService.search(keyword);
    }
    
    
    @PostMapping("/newrecipe")
    @ApiOperation(value = "Add new recipe", notes = "Add new recipe")
    public Recipes newRecipe(
            @RequestParam(value = "category_id", required = true) Integer category_id,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "intro", required = false) String intro,
            @RequestParam(value = "instruction", required = true) String instruction,
            @RequestParam(value = "image", required = false) String image,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "servings", required = false) String servings,
            @RequestParam(value = "difficulty", required = false) String difficulty,
            @RequestParam(value = "video", required = false) String video,
            @RequestParam(value = "username", required = true) String username
    ) {
        /*if (!SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal().equals("anonymousUser")) {*/
        Users u = usersService.findByUsername(username);
        if (u != null) {
            Categories c = new Categories();
            Categories cat = categoriesService.findAll().stream().filter(category -> category.getId() == category_id).findFirst().get();
            List<Recipes> maxIdRecipe = recipesService.maxId();
            Recipes recipe = new Recipes();
            recipe.setId(maxIdRecipe.get(0).getId() + 1);
            recipe.setCategory(cat);
            recipe.setName(name);
            recipe.setIntro(intro);
            recipe.setInstruction(instruction);
            recipe.setCalories(difficulty);
            recipe.setRating(0);
            recipe.setFavorite(0);
            recipe.setUser(u);
            recipe.setTime(time);
            recipe.setPosted(0);
            recipe.setLink("");
            recipe.setServings(servings);
            recipe.setVideo(video);
            recipe.setImage(image);
            recipe.setCategory_id(category_id);
            //System.out.println(recipe);
            return recipesService.save(recipe);
        } else {
            return null;
        }

/*
        } else return null;
*/
    }


    /*
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
