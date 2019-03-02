package al.recipes.rest.controllers;

import al.recipes.models.Categories;
import al.recipes.models.Ingredients;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        return recipesService.findAll(PageRequest.of(evalPage, INITIAL_PAGE_SIZE, sort));
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
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        return recipesService.findAllBycategory(cat, PageRequest.of(evalPage, INITIAL_PAGE_SIZE, sort));
    }
    
    @GetMapping("/search/{keyword}")
    @ApiOperation(value = "Search for recipes", notes = "Search for recipes")
    public List<Recipes> search(@PathVariable(value = "keyword") String keyword) {
        return recipesService.search(keyword);
    }
    
    @PostMapping("/setUserRecipe/{id}/{username}")
    @ApiOperation(value = "Set owner (user) for recipe", notes = "Set owner (user) for recipe")
    public void setUserRecipe(@PathVariable(value = "id") long recipeId, @PathVariable(value = "username") String username) {
        Users u = usersService.findByUsername(username);
        recipesService.updateUser(recipeId, u);
    }
    
    @PostMapping("/newrecipe")
    @CrossOrigin(origins = "http://localhost:3000")
    @ApiOperation(value = "Add new recipe", notes = "Add new recipe")
    @ResponseBody
    public String newRecipe(
            @RequestBody Map<String, String> body
    ) {
        Integer category_id = Integer.valueOf(body.get("category_id"));
        if (body.get("category_id") != null && category_id > 0) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            Categories c = new Categories();
            Categories cat = categoriesService.findAll().stream().filter(category -> category.getId() == category_id).findFirst().get();
            List<Recipes> maxIdRecipe = recipesService.maxId();
            Recipes recipe = new Recipes();
            recipe.setId(maxIdRecipe.get(0).getId() + 1);
            recipe.setCategory(cat);
            recipe.setName(body.get("name"));
            recipe.setIntro(body.get("intro").replace("\\n", "<br>"));
            recipe.setInstruction(body.get("instruction"));
            recipe.setCalories(body.get("difficulty"));
            recipe.setRating(0);
            recipe.setFavorite(0);
            //recipe.setUser(u);
            recipe.setTime(body.get("time"));
            recipe.setPosted(Integer.valueOf(today.format(formatter)));
            recipe.setLink("");
            recipe.setServings(body.get("servings"));
            recipe.setVideo(body.get("video"));
            recipe.setImage(body.get("image"));
            recipe.setCategory_id(category_id);
            List<Ingredients> ingrs = new ArrayList<>();
            ingrs = addIngredient(body.get("ingredient1"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient2"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient3"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient4"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient5"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient6"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient7"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient8"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient9"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient10"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient11"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient12"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient13"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient14"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient15"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient16"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient17"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient18"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient19"), recipe.getId(), ingrs);
            ingrs = addIngredient(body.get("ingredient20"), recipe.getId(), ingrs);
            recipe.setIngredients(ingrs);
            String idCreated = recipesService.save(recipe).getId().toString();
            return "http://localhost/recipe/" + idCreated;
        } else {
            return null;
        }
    }
    
    private List<Ingredients> addIngredient(String name, Long id, List<Ingredients> list) {
        if (!name.replace(" ", "").isEmpty()) {
            Ingredients in = new Ingredients();
            if (list.isEmpty()) {
                in.setId(recipesService.maxIngrId() + 1);
            } else {
                in.setId(getLocalMaxIngrId(list) + 1);
            }
            in.setRecipe_id(id.intValue());
            in.setName(name);
            list.add(in);
        }
        return list;
    }
    
    public Long getLocalMaxIngrId(List<Ingredients> list) {
        list.sort(Comparator.comparingLong(Ingredients::getId).reversed());
        return list.get(0).getId();
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
