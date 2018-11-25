package al.recipes.controllers;

import al.recipes.models.Recipe;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Controller
@RequestMapping("/")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private SoapClient categorySoapClient;
    @Autowired
    private TagControllerRest tagControllerRest;

    @RequestMapping(value = {"/", "/{page}", "/{page}/cat/{cat}"})
    public String getAllRecipes(Model model, @PathVariable(value = "page") Optional<Integer> p, @PathVariable Optional<Integer> cat) {
        List<Recipe> recipes = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        List<Categories> categories = new ArrayList<>();
        List<Recipe> recent_recipes = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        GetCategoriesResponse soapResponse = categorySoapClient.getCategories();
        soapResponse.getCategories().forEach(categories::add);
        Categories currentCat = null;
        Integer page = 1;
        if (p.isPresent()) page = p.get();
        String main_url = "http://localhost/api/recipes/" + page;
        String next_url = "http://localhost/api/recipes/" + (page + 1);
        String link_more = "/" + (page + 1);
        String tags_url = "http://localhost/api/tags";
        String recent_url = "http://localhost/api/recipes/1";

        if (cat.isPresent()) {
            main_url = "http://localhost/api/recipes/" + page + "/cat/" + cat.get();
            next_url = "http://localhost/api/recipes/" + (page + 1) + "/cat/" + cat.get();
            link_more = "/" + (page + 1) + "/cat/" + cat.get();
            currentCat = soapResponse.getCategories().stream().filter(c -> c.getId() == cat.get()).findFirst().get();
        }
        ResponseEntity<PagedResources<Recipe>> response =
                restTemplate.exchange(main_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Recipe>>() {
                        }
                );
        Collection<Recipe> recipes_arr = Objects.requireNonNull(response.getBody()).getContent();
        recipes_arr.stream().limit(20).forEach(recipes::add);

        ResponseEntity<PagedResources<Recipe>> recent_response =
                restTemplate.exchange(recent_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Recipe>>() {
                        }
                );
        Collection<Recipe> recent_recipes_arr = Objects.requireNonNull(recent_response.getBody()).getContent();
        recent_recipes_arr.stream().limit(10).forEach(recent_recipes::add);

        ResponseEntity<List<String>> tags_response =
                restTemplate.exchange(tags_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
                        }
                );
        Collection<String> tags_arr = Objects.requireNonNull(tags_response.getBody());
        tags_arr.stream().limit(20).forEach(tags::add);

        ResponseEntity<PagedResources<Recipe>> response_next =
                restTemplate.exchange(next_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Recipe>>() {
                        }
                );
        if (Objects.requireNonNull(response_next.getBody()).getContent().size() > 0) {
            model.addAttribute("link_more", link_more);
        }
        else {
            model.addAttribute("link_more", "#");
        }

        if (currentCat != null) {
            model.addAttribute("currentCat", currentCat);
        }
        model.addAttribute("recent", recent_recipes);
        model.addAttribute("tagCloud", tags);
        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        return "home";
    }

    /*private String translate(String txtToBeTranslated) {
        String txt = txtToBeTranslated;
        if (LocaleContextHolder.getLocale().toString().equals("en")) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity("https://www.googleapis.com/language/translate/" +
                    "v2?key=AIzaSyCbxc7thCFh8PQ9fmNJ_171bO3-jy_euqA&source=sq&target=en&q=" + txtToBeTranslated, String.class);

            txt = response.getBody().replace("{\n" +
                    "  \"data\": {\n" +
                    "    \"translations\": [\n" +
                    "      {\n" +
                    "        \"translatedText\": \"", "").replace("\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}", "");
        }
        if (txt.length() > 100) return txt.substring(0, 100) + "...";
        return txt;
    }*/
}

