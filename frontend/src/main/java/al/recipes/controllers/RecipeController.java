package al.recipes.controllers;

import al.recipes.models.Recipes;
import al.recipes.models.Tags;
import al.recipes.rest.controllers.TagControllerRest;
import al.recipes.soap.SoapClient;
import categories.wsdl.Categories;
import categories.wsdl.GetCategoriesResponse;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@RequestMapping("/")

public class RecipeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private SoapClient categorySoapClient;
    @Autowired
    private TagControllerRest tagControllerRest;
    @Autowired
    private MessageSource messageSource;
    
    @GetMapping("/recipe/{id}")
    @ApiOperation(value = "Get recipe by id and display the view_recipe template")
    public String index(Model model, @PathVariable long id) {
        List<Recipes> recipes = new ArrayList<>();
        List<Categories> categories = new ArrayList<>();
        List<String> tagCloud = new ArrayList<>();
        List<Recipes> recent_recipes = new ArrayList<>();
        
        RestTemplate restTemplate = new RestTemplate();
        String main_url = "http://localhost/api/recipe/" + id;
        String tag_cloud_url = "http://localhost/api/tags";
        String recent_url = "http://localhost/api/recipes/1";
        
        Recipes recipe = restTemplate.getForObject(main_url, Recipes.class, 200);
        List<Tags> tags = Objects.requireNonNull(recipe).getTags();
        tags.sort(Comparator.comparingInt(Tags::getStart_pos));
        GetCategoriesResponse soapResponse = categorySoapClient.getCategories();
        categories.addAll(soapResponse.getCategories());
        
        Categories currentCat = soapResponse.getCategories().stream().filter(c -> c.getId() == recipe.getCategory().getId()).findFirst().get();
        
        ResponseEntity<List<String>> tags_response =
                restTemplate.exchange(tag_cloud_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
                        }
                );
        Collection<String> tags_arr = Objects.requireNonNull(tags_response.getBody());
        tags_arr.stream().limit(20).forEach(tagCloud::add);
        
        ResponseEntity<PagedResources<Recipes>> recent_response =
                restTemplate.exchange(recent_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Recipes>>() {
                        }
                );
        Collection<Recipes> recent_recipes_arr = Objects.requireNonNull(recent_response.getBody()).getContent();
        recent_recipes_arr.stream().limit(10).forEach(recent_recipes::add);
        
        int start_pos = 0;
        int start_pos2 = 0;
        for (Tags tag : tags) {
            if (tag.getIntro_instruction().equals("instruction")) {
                StringBuilder ins = new StringBuilder(recipe.getInstruction());
                if (!ins.substring(tag.getStart_pos() + start_pos, tag.getEnd_pos() + start_pos).contains("<a") &&
                        !ins.substring(tag.getStart_pos() + start_pos2, tag.getEnd_pos() + start_pos2).contains("<a")) {
                    
                    recipe.setInstruction(ins.replace(
                            (tag.getStart_pos() + start_pos),
                            (tag.getEnd_pos() + start_pos),
                            "<a class='tags' href='/search/" + tag.getValue() + "'>" + tag.getValue() + "</a>"
                    ).toString());
                    start_pos2 = start_pos;
                    start_pos += ("<a class='tags' href='/search/" + tag.getValue() + "'>" + "</a>").length();
                }
            }
        }
        
        start_pos = 0;
        start_pos2 = 0;
        for (Tags tag : tags) {
            if (tag.getIntro_instruction().equals("intro")) {
                StringBuilder ins = new StringBuilder(recipe.getIntro());
                if (!ins.substring(tag.getStart_pos() + start_pos, tag.getEnd_pos() + start_pos).contains("<a") &&
                        !ins.substring(tag.getStart_pos() + start_pos2, tag.getEnd_pos() + start_pos2).contains("<a")) {
                    
                    recipe.setIntro(ins.replace(
                            (tag.getStart_pos() + start_pos),
                            (tag.getEnd_pos() + start_pos),
                            "<a class='tags' href='/search/" + tag.getValue() + "'>" + tag.getValue() + "</a>"
                    ).toString());
                    start_pos2 = start_pos;
                    start_pos += ("<a class='tags' href='/search/" + tag.getValue() + "'>" + "</a>").length();
                }
            }
        }
        if (currentCat != null) {
            model.addAttribute("currentCat", currentCat);
        }
        model.addAttribute("recent", recent_recipes);
        model.addAttribute("tagCloud", tagCloud);
        model.addAttribute("tags", tags);
        model.addAttribute("recipe", recipe);
        model.addAttribute("categories", categories);
        
        return "view_recipe";
    }
    
    /*@PostMapping("/addrecipe")
    public String newRecipe(Model model, @RequestParam(value = "id", required = true) long id,
                            @RequestParam(value = "category_id", required = true) int category_id,
                            @RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "intro", required = false) String intro,
                            @RequestParam(value = "instruction", required = true) String instruction,
                            @RequestParam(value = "image", required = false) String image,
                            @RequestParam(value = "link", required = false) String link,
                            @RequestParam(value = "time", required = false) String time,
                            @RequestParam(value = "servings", required = false) String servings,
                            @RequestParam(value = "calories", required = false) String calories,
                            @RequestParam(value = "favorite", required = false) int favorite,
                            @RequestParam(value = "rating", required = false) int rating,
                            @RequestParam(value = "posted", required = false) int posted,
                            @RequestParam(value = "video", required = false) String video,
                            @RequestParam(value = "username", required = true) String username) {
        System.out.println(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());
        
        if (!SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal().equals("anonymousUser")) {
            String url = "http://localhost/api/newrecipe?" + id;
            ResponseEntity<Recipes> response = new RestTemplate().postForEntity(url, null, Recipes.class);
            System.out.println(response.getBody().getId());
            return "redirect:/recipe/" + response.getBody().getId();
        } else {
            return "login";
        }
    }*/
    
    @GetMapping("/add")
    @ApiOperation(value = "Get template to add a new recipe")
    public String index(Model model, Locale locale) {
        final String page_title = messageSource.getMessage("add", null, locale);
        model.addAttribute("page_title", page_title);
        return "edit_recipe";
    }
}
