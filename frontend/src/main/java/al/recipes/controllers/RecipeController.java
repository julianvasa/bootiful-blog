package al.recipes.controllers;

import al.recipes.models.Recipes;
import al.recipes.models.Tags;
import al.recipes.rest.controllers.TagControllerRest;
import al.recipes.soap.SoapClient;
import categories.wsdl.Categories;
import categories.wsdl.GetCategoriesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/recipe/{id}")
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
        soapResponse.getCategories().forEach(categories::add);

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
                StringBuffer ins = new StringBuffer(recipe.getInstruction());
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
                StringBuffer ins = new StringBuffer(recipe.getIntro());
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
}
